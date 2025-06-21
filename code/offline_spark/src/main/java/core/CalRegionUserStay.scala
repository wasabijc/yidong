package core

import com.clickhouse.client.ClickHouseDataType
import com.clickhouse.client.data.ClickHouseBitmap
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, LongType, StringType, StructType}
import org.roaringbitmap.RoaringBitmap
import utils.ClickhouseTool

import java.util.Properties
import scala.collection.mutable

/**
 * 区域用户停留计算类
 * 功能：计算用户在指定区域的停留情况，并生成位图索引存储到ClickHouse
 * 执行频率：每天执行一次
 *
 * 输入数据：
 * 1. XDR数据（包含用户IMSI、位置信息和时间戳）
 * 2. 区域-基站映射数据
 * 3. 用户位图索引数据（从ClickHouse读取）
 *
 * 输出数据：
 * 区域用户位图（存储到ClickHouse的REGION_ID_IMSI_BITMAP表）
 */
object CalRegionUserStay {

  /**
   * 主方法，程序入口
   * @param args 命令行参数
   */
  def main(args: Array[String]): Unit = {
    // 1. 初始化Spark配置和会话
    val conf = new SparkConf().setMaster("local")
    val sparkSession = SparkSession.builder()
      .appName("CalRegionUserStay")
      .config(conf)
      .getOrCreate()

    // 2. 加载XDR数据（用户信令数据）
    val xdrPath = "hdfs://bigdata01:9000/xdr/20230204/*"
    val xdrStruct = new StructType()
      .add("imsi", StringType)      // 用户IMSI
      .add("laccell", StringType)   // 基站小区ID
      .add("lat", DoubleType)       // 纬度
      .add("lot", DoubleType)       // 经度
      .add("startTime", LongType)   // 开始时间戳（毫秒）

    val xdrDf = sparkSession.read
      .format("csv")
      .option("sep", "|")
      .schema(xdrStruct)
      .load(xdrPath)

    // 3. 加载区域-基站映射数据
    val regionCellPath = "hdfs://bigdata01:9000/data/regionCell"
    val regionCellStruct = new StructType()
      .add("regionId", StringType)  // 区域ID
      .add("laccell", StringType)   // 基站小区ID

    val regionCellDf = sparkSession.read
      .format("csv")
      .option("sep", "|")
      .schema(regionCellStruct)
      .load(regionCellPath)

    // 4. 从ClickHouse加载用户位图索引数据
    val url = "jdbc:clickhouse://bigdata01:8123"
    val prop = new Properties()
    prop.setProperty("user", "default")
    prop.setProperty("password", "clickhouse")

    val userBitmapIndex = sparkSession.read
      .jdbc(url, "USER_INDEX", prop)

    // 5. 创建临时视图以便SQL查询
    xdrDf.createOrReplaceTempView("xdr")
    regionCellDf.createOrReplaceTempView("region_cell")
    userBitmapIndex.createOrReplaceTempView("user_index")

    // 6. 定义SQL查询，计算区域用户停留情况
    val sql =
      """
        |SELECT
        |  tmp.regionId,
        |  tmp.produce_hour,
        |  COLLECT_SET(tmp.BITMAP_ID) AS bitmapIds
        |FROM (
        |  SELECT
        |    b.regionId,
        |    FROM_UNIXTIME(a.startTime/1000, 'yyyyMMddHH') AS produce_hour,
        |    c.BITMAP_ID
        |  FROM xdr a
        |  LEFT JOIN region_cell b ON a.laccell = b.laccell
        |  LEFT JOIN user_index c ON a.imsi = c.IMSI
        |) AS tmp
        |GROUP BY tmp.regionId, tmp.produce_hour
        |""".stripMargin

    // 7. 执行查询并将结果写入ClickHouse
    sparkSession.sql(sql).rdd.foreachPartition { it =>
      // 获取ClickHouse连接
      val conn = ClickhouseTool.getConn()

      // 处理每个分区的数据
      it.foreach { row =>
        val regionId = row.getAs[String]("regionId")          // 区域ID
        val produceHour = row.getAs[String]("produce_hour")   // 时间小时（格式：yyyyMMddHH）
        val bitmap = new RoaringBitmap()                      // 创建位图对象

        // 获取位图ID数组并添加到RoaringBitmap中
        val bitArr = row.getAs[mutable.WrappedArray[java.math.BigDecimal]]("bitmapIds")
        bitArr.foreach { x =>
          bitmap.add(x.intValue())
        }

        // 将位图转换为ClickHouse格式
        val ckBitmap = ClickHouseBitmap.wrap(bitmap, ClickHouseDataType.UInt32)

        // 准备并执行插入语句
        val stmt = conn.prepareStatement(
          "INSERT INTO REGION_ID_IMSI_BITMAP " +
            "(REGION_ID, PRODUCE_HOUR, IMSI_INDEXES) VALUES (?, ?, ?)")

        stmt.setString(1, regionId)
        stmt.setString(2, produceHour)
        stmt.setObject(3, ckBitmap)
        stmt.executeUpdate()
        stmt.close()
      }

      // 关闭连接
      conn.close()
    }

    // 8. 停止Spark会话
    sparkSession.stop()
  }
}