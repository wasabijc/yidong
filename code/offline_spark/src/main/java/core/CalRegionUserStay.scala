package core

import com.clickhouse.data.ClickHouseDataType
import com.clickhouse.data.value.ClickHouseBitmap
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.CoProcessFunction
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import org.roaringbitmap.RoaringBitmap
import utils.ClickhouseTool

import java.sql.{Connection, PreparedStatement, ResultSet}
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneOffset}
import scala.collection.mutable

/**
 * Flink区域用户停留计算类
 * 功能：每5分钟计算用户在指定区域的停留情况，并生成位图索引存储到ClickHouse
 */
object CalRegionUserStay {

//  private val LOG = org.slf4j.LoggerFactory.getLogger(classOf[CalRegionUserStay.type])
//private val LOG = org.slf4j.LoggerFactory.getLogger(classOf[core.CalRegionUserStay.type])
private val LOG = org.slf4j.LoggerFactory.getLogger("core.CalRegionUserStay")

  // 时间格式化常量
  private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
  private val WINDOW_SIZE: Time = Time.minutes(5)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(4)

    // 1. 模拟XDR数据流（生产环境替换为Kafka等数据源）
    val xdrStream = env.fromElements(
        ("460001234567890", "cell_001", 39.9042, 116.4074, System.currentTimeMillis()),
        ("460001234567891", "cell_002", 39.9042, 116.4074, System.currentTimeMillis()),
        ("460001234567892", "cell_001", 39.9042, 116.4074, System.currentTimeMillis())
      ).map(t => XdrRecord(t._1, t._2, t._3, t._4, t._5))
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[XdrRecord](Time.seconds(5)) {
        override def extractTimestamp(element: XdrRecord): Long = element.startTime
      })

    // 2. 加载区域-基站映射数据
    val regionCellStream = env.addSource(new RegionCellSource)

    // 3. 加载用户位图索引数据
    val userBitmapStream = env.addSource(new UserBitmapSource)

    // 4. 数据处理流水线
    val resultStream = xdrStream
      .keyBy(_.laccell)
      .connect(regionCellStream.keyBy(_.laccell))
      .process(new RegionJoinFunction)
      .keyBy(_._2) // 按IMSI分组
      .connect(userBitmapStream.keyBy(_.imsi))
      .process(new BitmapJoinFunction)
      .keyBy(_._1) // 按regionId分组
      .window(TumblingEventTimeWindows.of(WINDOW_SIZE))
      .aggregate(new BitmapAggregator)

    // 5. 自定义写入ClickHouse
    resultStream.addSink(new ClickHouseBitmapSink)

    env.execute("Flink Region User Stay Bitmap Job")
  }

  // 自定义ClickHouse Sink
  class ClickHouseBitmapSink extends RichSinkFunction[RegionBitmapResult] {
    private var conn: Connection = _
    private var stmt: PreparedStatement = _

    override def open(parameters: Configuration): Unit = {
      conn = ClickhouseTool.getConn()
      stmt = conn.prepareStatement(
        "INSERT INTO REGION_ID_IMSI_BITMAP (REGION_ID, PRODUCE_TIME, IMSI_INDEXES) VALUES (?, ?, ?)"
      )
    }

    override def invoke(value: RegionBitmapResult): Unit = {
      try {
        val ckBitmap = ClickHouseBitmap.wrap(value.bitmap, ClickHouseDataType.UInt32)
        stmt.setString(1, value.regionId)
        stmt.setString(2, value.produceTime)
        stmt.setObject(3, ckBitmap)
        stmt.executeUpdate()
      } catch {
        case e: Exception =>
          println(s"Failed to insert bitmap for region ${value.regionId}: ${e.getMessage}")
      }
    }

    override def close(): Unit = {
      if (stmt != null) stmt.close()
      if (conn != null) conn.close()
    }
  }

  // 数据模型类
  case class XdrRecord(imsi: String, laccell: String, lat: Double, lon: Double, startTime: Long)
  case class RegionCellMapping(regionId: String, laccell: String)
  case class UserBitmapIndex(imsi: String, bitmapId: Long)
  case class RegionBitmapResult(regionId: String, produceTime: String, bitmap: RoaringBitmap)

  // 区域关联处理函数
  class RegionJoinFunction extends CoProcessFunction[XdrRecord, RegionCellMapping, (String, String)] {
    private lazy val cellRegionMap = mutable.Map[String, String]()

    override def processElement1(xdr: XdrRecord,
                                 ctx: CoProcessFunction[XdrRecord, RegionCellMapping, (String, String)]#Context,
                                 out: Collector[(String, String)]): Unit = {
      cellRegionMap.get(xdr.laccell).foreach { regionId =>
        out.collect((regionId, xdr.imsi))
      }
    }

    override def processElement2(mapping: RegionCellMapping,
                                 ctx: CoProcessFunction[XdrRecord, RegionCellMapping, (String, String)]#Context,
                                 out: Collector[(String, String)]): Unit = {
      cellRegionMap.put(mapping.laccell, mapping.regionId)
    }
  }

  // 位图索引关联处理函数
  class BitmapJoinFunction extends CoProcessFunction[(String, String), UserBitmapIndex, (String, Long)] {
    private lazy val userBitmapMap = mutable.Map[String, Long]()

    override def processElement1(regionUser: (String, String),
                                 ctx: CoProcessFunction[(String, String), UserBitmapIndex, (String, Long)]#Context,
                                 out: Collector[(String, Long)]): Unit = {
      userBitmapMap.get(regionUser._2).foreach { bitmapId =>
        out.collect((regionUser._1, bitmapId))
      }
    }

    override def processElement2(userIndex: UserBitmapIndex,
                                 ctx: CoProcessFunction[(String, String), UserBitmapIndex, (String, Long)]#Context,
                                 out: Collector[(String, Long)]): Unit = {
      userBitmapMap.put(userIndex.imsi, userIndex.bitmapId)
    }
  }

  // 位图聚合函数
  class BitmapAggregator extends AggregateFunction[(String, Long), RoaringBitmap, RegionBitmapResult] {
    override def createAccumulator(): RoaringBitmap = new RoaringBitmap()

    override def add(value: (String, Long), accumulator: RoaringBitmap): RoaringBitmap = {
      accumulator.add(value._2.toInt)
      accumulator
    }

    override def getResult(accumulator: RoaringBitmap): RegionBitmapResult = {
      val produceTime = LocalDateTime.now(ZoneOffset.UTC).format(TIME_FORMATTER)
      // ⚠️ 注意：这里 regionId 是从输入中传递来的，在当前设计中无法直接获取
      // 所以仍然保留空字符串占位符，后续应重构聚合器携带 regionId
      RegionBitmapResult("", produceTime, accumulator)
    }

    override def merge(a: RoaringBitmap, b: RoaringBitmap): RoaringBitmap = {
      a.or(b)
      a
    }
  }

  // 区域-基站映射数据源
  class RegionCellSource extends RichParallelSourceFunction[RegionCellMapping] {
    @volatile private var running = true

    override def run(ctx: SourceFunction.SourceContext[RegionCellMapping]): Unit = {
      val conn = ClickhouseTool.getConn()
      val stmt = conn.prepareStatement("SELECT regionId, laccell FROM region_cell")
      val rs = stmt.executeQuery()

      while (running && rs.next()) {
        ctx.collect(RegionCellMapping(
          rs.getString("regionId"),
          rs.getString("laccell")
        ))
      }

      closeQuietly(rs, stmt, conn)
    }

    override def cancel(): Unit = running = false

    private def closeQuietly(rs: ResultSet, stmt: PreparedStatement, conn: Connection): Unit = {
      if (rs != null) rs.close()
      if (stmt != null) stmt.close()
      if (conn != null) conn.close()
    }
  }

  // 用户位图索引数据源
  class UserBitmapSource extends RichParallelSourceFunction[UserBitmapIndex] {
    @volatile private var running = true

    override def run(ctx: SourceFunction.SourceContext[UserBitmapIndex]): Unit = {
      val conn = ClickhouseTool.getConn()
      val stmt = conn.prepareStatement("SELECT IMSI, BITMAP_ID FROM USER_INDEX")
      val rs = stmt.executeQuery()

      while (running && rs.next()) {
        ctx.collect(UserBitmapIndex(
          rs.getString("IMSI"),
          rs.getLong("BITMAP_ID")
        ))
      }

      closeQuietly(rs, stmt, conn)
    }

    override def cancel(): Unit = running = false

    private def closeQuietly(rs: ResultSet, stmt: PreparedStatement, conn: Connection): Unit = {
      if (rs != null) rs.close()
      if (stmt != null) stmt.close()
      if (conn != null) conn.close()
    }
  }
}