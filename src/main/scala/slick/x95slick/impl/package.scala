package slick.x95slick

import slick.jdbc.JdbcProfile
import scala.concurrent.duration._

package object impl {
  implicit val defaultProfile: JdbcProfile = slick.jdbc.PostgresProfile
  implicit val defaultDatabaseTimeout: Duration = 10 second
}
