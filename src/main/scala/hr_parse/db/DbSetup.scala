package hr_parse.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration

trait DbSetup {
  val connectionString = "jdbc:postgresql://localhost:5432/ibatech"

  def dbSetup(clean: Boolean = false): Unit = {
    val config = new FluentConfiguration()
      .dataSource(connectionString, "postgres", "secret")
    val flyway = new Flyway(config)
    if (clean) flyway.clean()
    flyway.migrate()
  }
}
