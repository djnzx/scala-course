package hr_parse.db

import io.getquill._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.postgresql.ds.PGSimpleDataSource

object QuillApp extends App {
  val src = new PGSimpleDataSource {{
    setUser("postgres")
    setPassword("secret")
    setDatabaseName("ibatech")
  }}
  val config = new HikariConfig() {{
    setDataSource(src)
  }}
  val ctx = new PostgresJdbcContext(LowerCase, new HikariDataSource(config))
  import ctx._

  import v_ranks_current._
  ctx
    .run(query[v_ranks_current])
    .foreach { println }

}
