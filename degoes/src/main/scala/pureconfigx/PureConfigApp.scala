package pureconfigx

import pureconfig._
import pureconfig.generic.auto._

/**
  * class members mus have the same 
  * names as the properties in config file
  */
case class AppDetails(name: String)
case class PgConfig(driver: String, url: String, user: String, password: String)
case class AppConfig(app: AppDetails, db: PgConfig)

object PureConfigApp {
  val r: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]
}
