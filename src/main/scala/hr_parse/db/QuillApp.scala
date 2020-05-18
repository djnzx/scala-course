package hr_parse.db

import io.getquill._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.postgresql.ds.PGSimpleDataSource

object QuillApp extends App with DbSetup {
  dbSetup(
    true
  )

  println(org.postgresql.Driver.parseURL(
//    "jdbc:postgresql://postgres:secret@localhost:5432/ibatech"
    "jdbc:postgresql://bkrzkbwvsnfmha:bb53de0d398d89a27eecccf8127ccb80eac606ad8e105d3afecefff532d42536@ec2-54-175-117-212.compute-1.amazonaws.com:5432/d3jlb2m5jifo75"
    , null))
  System.exit(0)

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
