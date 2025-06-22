package core

import com.clickhouse.data.ClickHouseDataType
import com.clickhouse.data.value.ClickHouseBitmap
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import org.roaringbitmap.RoaringBitmap
import utils.ClickhouseTool

import java.sql.{Connection, PreparedStatement}

/**
 * Flink 用户画像位图生成器 (版本1.6.1)
 * 功能：根据用户属性（性别、年龄）生成位图索引并存储到ClickHouse
 */
object CalUserPersonBitMap {

  // 数据模型类
  case class UserInfo(imsi: String, gender: String, age: Int)
  case class UserBitmap(imsi: String, bitmapId: Int)
  case class PortraitRecord(portraitId: Int, portraitValue: String, comment: String, bitmapId: Int)
  case class PortraitResult(portraitId: Int, portraitValue: String, comment: String, bitmap: RoaringBitmap)

  // 画像位图聚合函数
  class PortraitBitmapAggregator extends AggregateFunction[
    PortraitRecord,
    (RoaringBitmap, String, String, Int),
    PortraitResult
  ] {
    override def createAccumulator(): (RoaringBitmap, String, String, Int) = {
      (new RoaringBitmap(), "", "", 0)
    }

    override def add(
                      value: PortraitRecord,
                      accumulator: (RoaringBitmap, String, String, Int)
                    ): (RoaringBitmap, String, String, Int) = {
      accumulator._1.add(value.bitmapId)
      (accumulator._1, value.portraitValue, value.comment, value.portraitId)
    }

    override def getResult(
                            accumulator: (RoaringBitmap, String, String, Int)
                          ): PortraitResult = {
      PortraitResult(accumulator._4, accumulator._2, accumulator._3, accumulator._1)
    }

    override def merge(
                        a: (RoaringBitmap, String, String, Int),
                        b: (RoaringBitmap, String, String, Int)
                      ): (RoaringBitmap, String, String, Int) = {
      a._1.or(b._1)
      (a._1, a._2, a._3, a._4)
    }
  }

  //入口
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    env.setParallelism(4)

    // 1. 模拟用户信息数据流（生产环境替换为Kafka等数据源）
    def userInfoStream = env.addSource(new UserInfoSource)

    // 2. 从ClickHouse加载用户位图索引数据
    def userBitmapStream = env.addSource(new UserBitmapSource)

    // 3. 关联用户信息和位图索引
    def connectedStream = userInfoStream
      .connect(userBitmapStream)
      .flatMap(new UserPortraitCoFlatMap)

    // 4. 按画像类型分组并聚合
    def keyedStream = connectedStream
      .keyBy(r => (r.portraitId, r.portraitValue)) // 自动推断为(Int, String)

    def resultStream = keyedStream
      .window(TumblingProcessingTimeWindows.of(Time.minutes(5)))
      .aggregate(new PortraitBitmapAggregator)

    // 5. 写入ClickHouse
    resultStream.addSink(new ClickHousePortraitSink)

    env.execute("Flink User Portrait Bitmap Job")

    // 用户信息数据源
    class UserInfoSource extends SourceFunction[UserInfo] {
      private var isRunning = true

      override def run(ctx: SourceFunction.SourceContext[UserInfo]): Unit = {
        // 模拟从HDFS读取数据
        val sampleData = Seq(
          UserInfo("460001234567890", "1", 25),
          UserInfo("460001234567891", "0", 35),
          UserInfo("460001234567892", "1", 15),
          UserInfo("460001234567893", "0", 45)
        )

        while (isRunning) {
          sampleData.foreach { user =>
            ctx.collect(user)
          }
          Thread.sleep(5000) // 5秒间隔模拟实时数据
        }
      }

      override def cancel(): Unit = {
        isRunning = false
      }
    }

    // 用户位图数据源
    class UserBitmapSource extends SourceFunction[UserBitmap] {
      private var isRunning = true

      override def run(ctx: SourceFunction.SourceContext[UserBitmap]): Unit = {
        val conn = ClickhouseTool.getConn()
        try {
          val stmt = conn.prepareStatement("SELECT IMSI, BITMAP_ID FROM USER_INDEX")
          val rs = stmt.executeQuery()

          while (isRunning && rs.next()) {
            ctx.collect(UserBitmap(rs.getString("IMSI"), rs.getInt("BITMAP_ID")))
          }

          rs.close()
          stmt.close()
        } finally {
          conn.close()
        }
      }

      override def cancel(): Unit = {
        isRunning = false
      }
    }

    // 修改为CoFlatMapFunction实现
    class UserPortraitCoFlatMap extends CoFlatMapFunction[UserInfo, UserBitmap, PortraitRecord] {
      // 存储用户信息缓存
      private var userInfoCache: Map[String, UserInfo] = Map()
      // 存储用户位图缓存
      private var userBitmapCache: Map[String, UserBitmap] = Map()

      // 处理用户信息流
      override def flatMap1(userInfo: UserInfo, out: Collector[PortraitRecord]): Unit = {
        userInfoCache += (userInfo.imsi -> userInfo)
        // 如果对应的位图已存在，则生成画像记录
        userBitmapCache.get(userInfo.imsi).foreach { bitmap =>
          generatePortraitRecords(userInfo, bitmap, out)
        }
      }

      // 处理用户位图流
      override def flatMap2(userBitmap: UserBitmap, out: Collector[PortraitRecord]): Unit = {
        userBitmapCache += (userBitmap.imsi -> userBitmap)
        // 如果对应的用户信息已存在，则生成画像记录
        userInfoCache.get(userBitmap.imsi).foreach { info =>
          generatePortraitRecords(info, userBitmap, out)
        }
      }

      // 生成画像记录
      private def generatePortraitRecords(userInfo: UserInfo, userBitmap: UserBitmap, out: Collector[PortraitRecord]): Unit = {
        // 只处理10岁以上的用户
        if (userInfo.age >= 10) {
          // 生成性别画像
          if (userInfo.gender == "1") {
            out.collect(PortraitRecord(1, "1", "男性", userBitmap.bitmapId))
          } else {
            out.collect(PortraitRecord(2, "0", "女性", userBitmap.bitmapId))
          }

          // 生成年龄段画像
          userInfo.age match {
            case age if age >= 10 && age < 20 =>
              out.collect(PortraitRecord(3, "10", "10-20岁", userBitmap.bitmapId))
            case age if age >= 20 && age < 40 =>
              out.collect(PortraitRecord(4, "20", "20-40岁", userBitmap.bitmapId))
            case age if age >= 40 =>
              out.collect(PortraitRecord(5, "40", "40岁以上", userBitmap.bitmapId))
            case _ => // 忽略
          }
        }
      }
    }



    // ClickHouse Sink
    class ClickHousePortraitSink extends RichSinkFunction[PortraitResult] {
      private var conn: Connection = _
      private var stmt: PreparedStatement = _

      override def open(parameters: Configuration): Unit = {
        conn = ClickhouseTool.getConn()
        stmt = conn.prepareStatement(
          """INSERT INTO TA_PORTRAIT_IMSI_BITMAP
            |(PORTRAIT_ID, PORTRAIT_VALUE, PORTRAIT_BITMAP, COMMENT)
            |VALUES (?, ?, ?, ?)""".stripMargin)
      }

      override def invoke(value: PortraitResult): Unit = {
        try {
          val ckBitmap = ClickHouseBitmap.wrap(value.bitmap, ClickHouseDataType.UInt32)
          stmt.setInt(1, value.portraitId)
          stmt.setString(2, value.portraitValue)
          stmt.setObject(3, ckBitmap)
          stmt.setString(4, value.comment)
          stmt.executeUpdate()
        } catch {
          case e: Exception =>
            println(s"Failed to insert portrait bitmap: ${e.getMessage}")
        }
      }

      override def close(): Unit = {
        if (stmt != null) stmt.close()
        if (conn != null) conn.close()
      }
    }
  }
}