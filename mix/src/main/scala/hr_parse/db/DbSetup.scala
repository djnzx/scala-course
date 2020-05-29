package hr_parse.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration

trait DbSetup {
  val url = "jdbc:postgresql://localhost:5432/ibatech"
  val username = "postgres"
  val password = "secret"

  def dbSetup(clean: Boolean = false): Unit = {
    val config = new FluentConfiguration()
      .dataSource(url, username, password)
    val flyway = new Flyway(config)
    if (clean) flyway.clean()
    flyway.migrate()
  }
}
