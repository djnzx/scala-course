package hr_parse.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration

trait DbSetup {
  val connectionString = "jdbc:postgresql://localhost:5432/ibatech"

  def dbSetup(): Unit = {
    val config = new FluentConfiguration()
      .dataSource(connectionString, "postgres", "secret")
    val flyway = new Flyway(config)
//    flyway.clean()
    flyway.migrate()
  }
}
