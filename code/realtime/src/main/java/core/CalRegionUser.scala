package core

import dto.{RegionCal, RegionCell, UserInfo, UserRegionFlow, UserXdr, XDR}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.api.java.io.TextInputFormat
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.hbase.sink.{HBaseMutationConverter, HBaseSinkFunction}
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.co.{BroadcastProcessFunction, KeyedBroadcastProcessFunction}
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import org.apache.hadoop.conf
import org.apache.hadoop.hbase.client.{Mutation, Put}

import java.util.Properties

/**
 * 区域用户统计计算程序
 *
 * 功能：实时计算各行政区人员构成情况（性别、年龄分布）
 *
 * 主要处理流程：
 * 1. 读取Kafka中的信令数据
 * 2. 加载用户基础数据和基站-区域映射数据
 * 3. 融合信令数据和用户基础数据
 * 4. 分析用户行政区流动情况
 * 5. 按行政区统计人员构成（每分钟输出一次）
 * 6. 将结果写入HBase
 *
 * 数据流：
 * Kafka(XDR) → 融合用户数据 → 分析区域流动 → 区域统计 → HBase
 */
object CalRegionUser {

  def main(args: Array[String]): Unit = {
    // 1. 初始化Flink执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._

    // 2. 配置Kafka消费者
    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "bigdata01:9092")
    val kafkaConsumer = new FlinkKafkaConsumer[String](
      "xdr",
      new SimpleStringSchema(),
      prop
    )

    // 3. 读取信令数据（Kafka）
    val xdr = env.addSource(kafkaConsumer)
      .map(x => XDR.fromKafka(x))  // 解析原始信令数据

    // 4. 加载用户基础数据（HDFS）
    val userInfoPath = "hdfs://bigdata01:9000/data/userInfo"
    val userInfoStream = env.readFile(
      new TextInputFormat(new Path(userInfoPath)),
      userInfoPath,
      FileProcessingMode.PROCESS_CONTINUOUSLY,  // 持续监控文件变化
      1000  // 监控间隔(ms)
    ).map(x => UserInfo.fromStr(x))  // 解析用户数据

    // 5. 加载基站-区域映射数据（HDFS）
    val regionCellPath = "hdfs://bigdata01:9000/data/regionCell"
    val regionCellStream = env.readFile(
      new TextInputFormat(new Path(regionCellPath)),
      regionCellPath,
      FileProcessingMode.PROCESS_CONTINUOUSLY,
      1000
    ).map(x => RegionCell.fromStr(x))  // 解析基站-区域映射

    // 6. 定义广播状态描述符（用于广播用户数据）
    val broadcastUserInfoDes = new MapStateDescriptor[String, UserInfo](
      "broadcastUserInfoDes",
      classOf[String],  // Key类型：IMSI
      classOf[UserInfo] // Value类型：用户信息
    )

    // 7. 融合信令数据和用户基础数据
    val userXdrStream = xdr.connect(userInfoStream.broadcast(broadcastUserInfoDes))
      .process(new BroadcastProcessFunction[XDR, UserInfo, UserXdr] {

        // 处理信令数据（使用广播的用户数据）
        override def processElement(
                                     in1: XDR,
                                     readOnlyContext: BroadcastProcessFunction[XDR, UserInfo, UserXdr]#ReadOnlyContext,
                                     collector: Collector[UserXdr]
                                   ): Unit = {
          val imsi = in1.imsi
          val laccell = in1.laccell
          val broadcastState = readOnlyContext.getBroadcastState(broadcastUserInfoDes)
          val userinfo = broadcastState.get(imsi)

          if (userinfo != null) {
            // 融合信令和用户数据
            collector.collect(
              UserXdr(imsi, laccell, userinfo.gender, userinfo.age)
            )
          }
        }

        // 处理广播的用户数据
        override def processBroadcastElement(
                                              in2: UserInfo,
                                              context: BroadcastProcessFunction[XDR, UserInfo, UserXdr]#Context,
                                              collector: Collector[UserXdr]
                                            ): Unit = {
          val broadcastState = context.getBroadcastState(broadcastUserInfoDes)
          broadcastState.put(in2.imsi, in2)  // 更新广播状态
        }
      })

    // 8. 定义广播状态描述符（用于广播基站-区域映射）
    val broadcastRegionCellDes = new MapStateDescriptor[String, String](
      "broadcastRegionCellDes",
      classOf[String],  // Key类型：基站ID
      classOf[String]   // Value类型：区域ID
    )

    // 9. 分析用户行政区流动情况
    val userRegionFlowStream = userXdrStream
      .keyBy(_.imsi)  // 按用户IMSI分组
      .connect(regionCellStream.broadcast(broadcastRegionCellDes))
      .process(new KeyedBroadcastProcessFunction[String, UserXdr, RegionCell, UserRegionFlow] {

        // 状态：记录用户上一个所在的区域
        var lastRegion: ValueState[String] = _

        override def open(parameters: Configuration): Unit = {
          super.open(parameters)
          // 初始化状态
          val lastRegionDes = new ValueStateDescriptor[String](
            "lastRegion",
            createTypeInformation[String]
          )
          lastRegion = getRuntimeContext.getState(lastRegionDes)
        }

        // 处理信令数据（分析区域流动）
        override def processElement(
                                     in1: UserXdr,
                                     readOnlyContext: KeyedBroadcastProcessFunction[String, UserXdr, RegionCell, UserRegionFlow]#ReadOnlyContext,
                                     collector: Collector[UserRegionFlow]
                                   ): Unit = {
          val laccell = in1.laccell
          val gender = in1.gender
          val age = in1.age

          // 获取当前基站对应的区域
          val broadcastState = readOnlyContext.getBroadcastState(broadcastRegionCellDes)
          val regionId = Option(broadcastState.get(laccell)).getOrElse("")

          // 检查区域是否发生变化
          if (!regionId.equals(lastRegion.value()) && regionId.nonEmpty) {
            // 生成流入新区域的记录
            collector.collect(UserRegionFlow(1, regionId, gender, age))

            // 如果之前有区域，生成流出旧区域的记录
            if (lastRegion.value() != null) {
              collector.collect(UserRegionFlow(0, lastRegion.value(), gender, age))
            }

            // 更新用户当前区域
            lastRegion.update(regionId)
          }
        }

        // 处理广播的基站-区域映射数据
        override def processBroadcastElement(
                                              in2: RegionCell,
                                              context: KeyedBroadcastProcessFunction[String, UserXdr, RegionCell, UserRegionFlow]#Context,
                                              collector: Collector[UserRegionFlow]
                                            ): Unit = {
          val broadcastState = context.getBroadcastState(broadcastRegionCellDes)
          broadcastState.put(in2.laccell, in2.regionId)  // 更新基站-区域映射
        }
      })

    // 10. 按行政区统计人员构成
    val regionCalStream = userRegionFlowStream
      .keyBy(_.regionId)  // 按区域ID分组
      .process(new KeyedProcessFunction[String, UserRegionFlow, RegionCal]() {

        // 状态：保存当前区域的统计结果
        var cal: ValueState[RegionCal] = _
        // 状态：标记是否已设置定时器
        var isSetTime: ValueState[Boolean] = _

        override def open(parameters: Configuration): Unit = {
          super.open(parameters)
          // 初始化状态
          val calDes = new ValueStateDescriptor[RegionCal](
            "cal",
            createTypeInformation[RegionCal]
          )
          cal = getRuntimeContext.getState(calDes)

          val isSetTimeDes = new ValueStateDescriptor[Boolean](
            "isSetTime",
            createTypeInformation[Boolean]
          )
          isSetTime = getRuntimeContext.getState(isSetTimeDes)
        }

        // 处理区域流动数据，更新统计结果
        override def processElement(
                                     value: UserRegionFlow,
                                     context: KeyedProcessFunction[String, UserRegionFlow, RegionCal]#Context,
                                     collector: Collector[RegionCal]
                                   ): Unit = {
          // 获取或初始化区域统计状态
          val regionCal = Option(cal.value()).getOrElse(
            RegionCal(value.regionId, 0, 0, 0, 0, 0)
          )

          // 根据流入/流出更新统计
          val addNum = if (value.isIn == 1) 1 else -1
          val gender = value.gender
          val age = value.age

          // 更新性别统计
          if (gender == 1) {
            regionCal.manNum += addNum
          } else {
            regionCal.womanNum += addNum
          }

          // 更新年龄段统计
          if (age >= 10 && age < 20) {
            regionCal.age_10_20 += addNum
          } else if (age >= 20 && age < 40) {
            regionCal.age_20_40 += addNum
          } else if (age >= 40) {
            regionCal.age_40 += addNum
          }

          // 更新状态
          cal.update(regionCal)

          // 设置定时器（每分钟触发一次输出）
          if (!Option(isSetTime.value()).getOrElse(false)) {
            context.timerService().registerProcessingTimeTimer(
              context.timerService().currentProcessingTime() + 60000
            )
            isSetTime.update(true)
          }
        }

        // 定时器触发时输出当前统计结果
        override def onTimer(
                              timestamp: Long,
                              ctx: KeyedProcessFunction[String, UserRegionFlow, RegionCal]#OnTimerContext,
                              out: Collector[RegionCal]
                            ): Unit = {
          val regionCal = cal.value()
          out.collect(regionCal)  // 输出统计结果
          isSetTime.update(false) // 重置定时器标记
        }
      })

    // 11. 打印统计结果（调试用）
    regionCalStream.print()

    // 12. 配置HBase Sink
    val conf = new conf.Configuration()
    conf.set("hbase.zookeeper.quorum", "bigdata01:2181")
    conf.set("hbase.rootdir", "hdfs://bigdata01:9000/hbase")

    val hbaseSink = new HBaseSinkFunction[RegionCal](
      "user_persona",  // HBase表名
      conf,
      new HBaseMutationConverter[RegionCal] {
        override def open(): Unit = {}  // 初始化方法

        // 将统计结果转换为HBase Put操作
        override def convertToMutation(record: RegionCal): Mutation = {
          val rowkey = record.regionId
          val put = new Put(rowkey.getBytes())

          // 添加各统计字段
          put.addColumn("persona".getBytes(), "man".getBytes(), record.manNum.toString.getBytes())
          put.addColumn("persona".getBytes(), "women".getBytes(), record.womanNum.toString.getBytes())
          put.addColumn("persona".getBytes(), "age_10_20".getBytes(), record.age_10_20.toString.getBytes())
          put.addColumn("persona".getBytes(), "age_20_40".getBytes(), record.age_20_40.toString.getBytes())
          put.addColumn("persona".getBytes(), "age_40".getBytes(), record.age_40.toString.getBytes())

          put
        }
      },
      100,    // 并行度
      100,    // 最大缓冲记录数
      1000    // 缓冲超时时间(ms)
    )

    // 13. 添加HBase Sink并执行作业
    regionCalStream.addSink(hbaseSink)
    env.execute("Region User Statistics Job")
  }
}