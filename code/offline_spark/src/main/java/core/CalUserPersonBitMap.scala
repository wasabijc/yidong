package core

import com.clickhouse.client.ClickHouseDataType
import com.clickhouse.client.data.ClickHouseBitmap
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.{SparkConf, SparkContext}
import org.roaringbitmap.RoaringBitmap
import utils.ClickhouseTool

import java.util.Properties
import scala.collection.mutable

/**
 * 用户画像位图生成器
 *
 * 功能：根据用户属性（性别、年龄）生成位图索引并存储到ClickHouse
 *
 * 位图类型定义：
 * | ID | 属性值 | 描述       |
 * |----|--------|------------|
 * | 1  | 1      | 男性用户位图 |
 * | 2  | 0      | 女性用户位图 |
 * | 3  | 10     | 10-20岁用户 |
 * | 4  | 20     | 20-40岁用户 |
 * | 5  | 40     | 40岁以上用户 |
 *
 * 输入数据：
 * 1. 用户基本信息（HDFS：/data/userInfo，格式：IMSI|性别|年龄）
 * 2. 用户位图索引表（ClickHouse：USER_INDEX）
 *
 * 输出数据：
 * 用户画像位图表（ClickHouse：TA_PORTRAIT_IMSI_BITMAP）
 */
object CalUserPersonBitMap {

  /**
   * 主入口方法
   * @param args 命令行参数（未使用）
   */
  def main(args: Array[String]): Unit = {
    // 1. 初始化Spark配置和会话
    val conf = new SparkConf()
      .setMaster("local")  // 本地模式（生产环境应移除）
    val sparkSession = SparkSession.builder()
      .appName("CalUserPersonBitMap")
      .config(conf)
      .getOrCreate()

    // 2. 定义用户信息数据结构
    val userInfoStruct = new StructType()
      .add("imsi", StringType)    // 用户唯一标识
      .add("gender", StringType)  // 性别（0:女，1:男）
      .add("age", IntegerType)    // 年龄

    // 3. 加载用户信息数据
    val userInfoDf = sparkSession.read
      .format("csv")
      .option("sep", "|")
      .schema(userInfoStruct)
      .load("hdfs://bigdata01:9000/data/userInfo")

    // 4. 创建临时视图
    userInfoDf.createOrReplaceTempView("user_info")

    // 5. 配置ClickHouse连接参数
    val url = "jdbc:clickhouse://bigdata01:8123"
    val prop = new Properties()
    prop.setProperty("user", "default")
    prop.setProperty("password", "clickhouse")

    // 6. 加载用户位图索引数据
    val userBitmapIndex = sparkSession.read
      .jdbc(url, "USER_INDEX", prop)
    userBitmapIndex.createOrReplaceTempView("user_index")

    // 7. 定义SQL查询（生成用户画像位图）
    val sql =
      """
        |-- 步骤1：预处理用户数据，添加年龄分段标记
        |WITH tmp_res AS (
        |  SELECT
        |    ui.BITMAP_ID,
        |    gender,
        |    age_flag
        |  FROM (
        |    SELECT
        |      imsi,
        |      gender,
        |      CASE
        |        WHEN (age >= 10 AND age < 20) THEN '10'
        |        WHEN (age >= 20 AND age < 40) THEN '20'
        |        WHEN (age >= 40) THEN '40'
        |      END AS age_flag
        |    FROM user_info
        |    WHERE age >= 10  -- 只处理10岁以上的用户
        |  ) AS tmp
        |  JOIN user_index AS ui ON tmp.imsi = ui.IMSI
        |)
        |
        |-- 步骤2：生成性别位图（男性/女性）
        |SELECT
        |  CASE
        |    WHEN gender = '1' THEN 1
        |    WHEN gender = '0' THEN 2
        |  END AS PORTRAIT_ID,
        |  gender AS PORTRAIT_VALUE,
        |  CASE
        |    WHEN gender = '1' THEN '男性'
        |    WHEN gender = '0' THEN '女性'
        |  END AS COMMENT,
        |  COLLECT_SET(BITMAP_ID) AS bitmapIds
        |FROM tmp_res
        |GROUP BY gender
        |
        |UNION
        |
        |-- 步骤3：生成年龄段位图
        |SELECT
        |  CASE
        |    WHEN age_flag = '10' THEN 3
        |    WHEN age_flag = '20' THEN 4
        |    WHEN age_flag = '40' THEN 5
        |  END AS PORTRAIT_ID,
        |  age_flag AS PORTRAIT_VALUE,
        |  CASE
        |    WHEN age_flag = '10' THEN '10-20岁'
        |    WHEN age_flag = '20' THEN '20-40岁'
        |    WHEN age_flag = '40' THEN '40岁以上'
        |  END AS COMMENT,
        |  COLLECT_SET(BITMAP_ID) AS bitmapIds
        |FROM tmp_res
        |GROUP BY age_flag
        |""".stripMargin

    // 8. 执行查询并写入ClickHouse
    sparkSession.sql(sql).rdd.foreachPartition { partition =>
      // 获取ClickHouse连接（每个分区一个连接）
      val conn = ClickhouseTool.getConn()

      try {
        // 处理当前分区的所有记录
        partition.foreach { row =>
          // 解析查询结果
          val id = row.getAs[Int]("PORTRAIT_ID")          // 位图类型ID
          val value = row.getAs[String]("PORTRAIT_VALUE") // 属性值
          val comment = row.getAs[String]("COMMENT")       // 描述

          // 创建位图并添加用户ID
          val bitmap = new RoaringBitmap()
          val bitArr = row.getAs[mutable.WrappedArray[java.math.BigDecimal]]("bitmapIds")
          bitArr.foreach { x =>
            bitmap.add(x.intValue())
          }

          // 转换为ClickHouse位图格式
          val ckBitmap = ClickHouseBitmap.wrap(bitmap, ClickHouseDataType.UInt32)

          // 准备并执行插入语句
          val stmt = conn.prepareStatement(
            """INSERT INTO TA_PORTRAIT_IMSI_BITMAP
              |(PORTRAIT_ID, PORTRAIT_VALUE, PORTRAIT_BITMAP, COMMENT)
              |VALUES (?, ?, ?, ?)""".stripMargin)

          stmt.setInt(1, id)
          stmt.setString(2, value)
          stmt.setObject(3, ckBitmap)
          stmt.setString(4, comment)
          stmt.executeUpdate()
          stmt.close()
        }
      } finally {
        // 确保连接关闭
        conn.close()
      }
    }

    // 9. 停止Spark会话
    sparkSession.stop()
  }
}