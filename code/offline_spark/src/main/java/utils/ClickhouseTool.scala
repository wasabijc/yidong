package utils

import com.clickhouse.jdbc.ClickHouseDataSource
import java.util.Properties

object ClickhouseTool {

  def getConn() = {
    val url = "jdbc:clickhouse://bigdata01:8123"
    val prop = new Properties()

    // 新版使用标准的 JDBC 属性设置用户名和密码
    prop.setProperty("user", "default")
    prop.setProperty("password", "clickhouse")

    val dataSource = new ClickHouseDataSource(url, prop)
    val conn = dataSource.getConnection
    conn
  }
}