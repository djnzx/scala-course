package hr_parse.db

import io.getquill._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.postgresql.ds.PGSimpleDataSource

object QuillApp extends App with DbSetup {
  dbSetup(true)

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
  import ctx._

  import v_ranks_current._
  ctx
    .run(query[v_ranks_current])
    .foreach { println }
}
