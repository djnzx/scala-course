package hr_parse.db

import io.getquill._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.postgresql.ds.PGSimpleDataSource

class QuillConn {
  val pgSrc = new PGSimpleDataSource {{
    setServerNames(Array("localhost"))
    setPortNumbers(Array(5432))
    setDatabaseName("ibatech")
    setUser("postgres")
    setPassword("secret")
  }}
  val config = new HikariConfig() {{
    setDataSource(pgSrc)
  }}
  val ctx = new PostgresJdbcContext(LowerCase,
    new HikariDataSource(config))
}
