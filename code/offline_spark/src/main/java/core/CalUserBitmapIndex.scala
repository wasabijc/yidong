package core

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import utils.ClickhouseTool

import java.sql.{Connection, Statement}

/**
 * Flink 用户位图索引生成器 (版本1.6.1)
 *
 * 功能：为每个用户生成唯一的位图ID并存储到ClickHouse的USER_INDEX表中
 *
 * 主要处理流程：
 * 1. 从HDFS加载用户信息数据（格式：IMSI|性别|年龄）
 * 2. 为每个用户分配唯一的数字ID
 * 3. 将用户IMSI和对应的唯一ID写入ClickHouse
 */
object CalUserBitmapIndex {

  // 用户数据模型
  case class UserRecord(imsi: String, var bitmapId: Long = 0L)

  /**
   * 自定义数据源 - 模拟从HDFS读取数据
   */
  class UserDataSource extends SourceFunction[String] {
    private var isRunning = true

    override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
      // 模拟数据（生产环境替换为实际HDFS路径）
      val sampleData = Seq(
        "460001234567890|1|25",
        "460001234567891|0|35",
        "460001234567892|1|15"
      )

      while (isRunning) {
        sampleData.foreach { line =>
          ctx.collect(line)
        }
        Thread.sleep(5000) // 模拟批处理间隔
      }
    }

    override def cancel(): Unit = {
      isRunning = false
    }
  }

  /**
   * 主入口方法
   */
  def main(args: Array[String]): Unit = {
    // 1. 初始化Flink环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2) // 设置并行度

    // 2. 创建数据源（生产环境替换为HDFS源）
    val userStream = env.addSource(new UserDataSource)

    // 3. 处理数据流
    val processedStream = userStream
      .map(_.split("\\|")) // 解析每行数据
      .map(arr => UserRecord(arr(0))) // 提取IMSI
      .map(new RichMapFunction[UserRecord, UserRecord] {
        private var idCounter = 0L

        override def open(parameters: Configuration): Unit = {
          // 初始化ID计数器（每个并行任务独立计数）
          idCounter = getRuntimeContext.getIndexOfThisSubtask * 1000000L
        }

        override def map(value: UserRecord): UserRecord = {
          value.bitmapId = idCounter
          idCounter += 1
          value
        }
      })

    // 4. 写入ClickHouse
    processedStream.addSink(new RichSinkFunction[UserRecord] {
      private var conn: Connection = _
      private var stmt: Statement = _

      override def open(parameters: Configuration): Unit = {
        conn = ClickhouseTool.getConn()
        stmt = conn.createStatement()
      }

      override def invoke(value: UserRecord): Unit = {
        val sql =
          s"""
            INSERT INTO USER_INDEX
            (IMSI, BITMAP_ID)
            VALUES('${value.imsi}', ${value.bitmapId})
          """
        stmt.executeUpdate(sql)
      }

      override def close(): Unit = {
        if (stmt != null) stmt.close()
        if (conn != null) conn.close()
      }
    })

    // 5. 执行任务
    env.execute("Flink User Bitmap Index Job")
  }
}