package pureconfigx

import pureconfig._
import pureconfig.generic.auto._

/**
  * class members mus have the same 
  * names as the properties in config file
  */
case class AppConf(name: String)
case class PgConfig(url: String, user: String, password: String)
case class WholeConf(app: AppConf, db: PgConfig)

object PureConfigApp extends App {
  val r: WholeConf = ConfigSource.default.loadOrThrow[WholeConf]
  pprint.pprintln(r)
}
