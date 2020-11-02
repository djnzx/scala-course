package nomicon.ch02.deps

import zio._
import pureconfig._
import pureconfig.generic.auto._

case class Configuration(appName: String)

trait ConfigurationModule {
  val configurationModule: ConfigurationModule.Service[Any]
}

object ConfigurationModule {

  case class CfError(message: String) extends RuntimeException(message)
  case class CfItem(appName: String)

  trait Service[R] {
    def configuration: ZIO[R, Throwable, CfItem]
  }

  trait Live extends ConfigurationModule {
    val configurationModule: ConfigurationModule.Service[Any] = new Service[Any] {
      override def configuration: Task[CfItem] =
        ZIO
          .fromEither(ConfigSource.default.load[CfItem])
          .mapError(e => CfError(e.toList.mkString(", ")))
    }
  }

  object factory extends ConfigurationModule.Service[ConfigurationModule] {
    override def configuration: ZIO[ConfigurationModule, Throwable, CfItem] =
      ZIO.accessM[ConfigurationModule](cm => cm.configurationModule.configuration)
  }
}
