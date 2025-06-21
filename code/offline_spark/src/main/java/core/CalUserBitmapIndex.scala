package core

import com.clickhouse.jdbc.ClickHouseDataSource
import org.apache.spark.{SparkConf, SparkContext}
import ru.yandex.clickhouse.settings.ClickHouseQueryParam
import utils.ClickhouseTool

import java.util.Properties

/**
 * 用户位图索引生成器
 *
 * 功能：为每个用户生成唯一的位图ID并存储到ClickHouse的USER_INDEX表中
 *
 * 主要处理流程：
 * 1. 从HDFS加载用户信息数据（格式：IMSI|性别|年龄）
 * 2. 为每个用户分配唯一的数字ID（使用zipWithUniqueId）
 * 3. 将用户IMSI和对应的唯一ID写入ClickHouse
 *
 * 执行频率：根据用户信息变化情况定期执行
 *
 * 输出表结构（ClickHouse的USER_INDEX表）：
 * | 列名     | 类型    | 描述          |
 * |----------|---------|-------------|
 * | IMSI     | String  | 用户唯一标识    |
 * | BITMAP_ID| UInt64  | 用户位图唯一ID  |
 */
object CalUserBitmapIndex {

  /**
   * 主入口方法
   *
   * @param args 命令行参数（未使用）
   */
  def main(args: Array[String]): Unit = {
    // 1. 初始化Spark配置
    val conf = new SparkConf()
      .setAppName("CalUserBitmapIndex")  // 设置应用名称
      .setMaster("local")                // 本地模式运行（生产环境应移除）

    // 2. 创建SparkContext
    val sc = new SparkContext(conf)

    // 3. 从HDFS加载用户信息数据并处理
    sc.textFile("hdfs://bigdata01:9000/data/userInfo")  // 读取HDFS文件
      .map { line =>
        // 3.1 解析每行数据，提取IMSI
        val arr = line.split("\\|")  // 按竖线分割
        arr(0)                       // 返回IMSI（第一列）
      }
      // 3.2 为每个用户分配唯一ID（延迟计算，不立即执行）
      .zipWithUniqueId()
      // 3.3 将结果写入ClickHouse（按分区并行处理）
      .foreachPartition { partition =>
        // 获取ClickHouse连接（每个分区一个连接）
        val conn = ClickhouseTool.getConn()
        val stmt = conn.createStatement()

        try {
          // 处理当前分区的所有记录
          partition.foreach { case (imsi, id) =>
            // 构造并执行插入语句
            val sql =
              s"""
                INSERT INTO USER_INDEX
                (IMSI, BITMAP_ID)
                VALUES('${imsi}','${id}')
              """
            stmt.executeUpdate(sql)
          }
        } finally {
          // 确保连接关闭
          stmt.close()
          conn.close()
        }
      }

    // 4. 停止SparkContext释放资源
    sc.stop()
  }
}