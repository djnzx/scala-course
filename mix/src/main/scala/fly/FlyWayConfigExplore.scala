package fly

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration

object FlyWayConfigExplore extends App {

  val c = new FluentConfiguration()
  val flyway = new Flyway(c)
  flyway.migrate()

}
