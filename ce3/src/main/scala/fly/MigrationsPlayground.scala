package fly

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

object MigrationsPlayground extends App {

  def mkUrl(host: String, dbName: String, port: Int) = s"jdbc:postgresql://$host:$port/$dbName"

  def runMigrations(url: String, user: String, pass: String): MigrateResult =
    Flyway.configure()
      .dataSource(url, user, pass)
      .locations("classpath:db/migration")
      .baselineOnMigrate(true)
      .failOnMissingLocations(true)
      .outOfOrder(false)
      .validateMigrationNaming(true)
      .placeholders(java.util.Map.of("schema", "public"))
      .schemas("public")
      .load()
      .migrate()

  val host = "localhost"
  val dbName = "test123"
  val port = 5432
  val user = "postgres"
  val pass = ???

  val url = mkUrl(host, dbName, port)

  runMigrations(url, user, pass)

}
