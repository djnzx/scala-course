package nomicon.ch02layer.services

import pureconfig._
import pureconfig.generic.auto._
import nomicon.ch02layer.services.Aliases.Configuration
import pureconfigx.AppConfig
import zio.{Has, UIO, ULayer, URIO, ZIO, ZLayer}

object Configuration {
  
  trait Service {
    def conf: UIO[AppConfig]
  }
  
  def conf: URIO[Configuration, AppConfig] = ZIO.accessM(_.get.conf)

  val file: ULayer[Has[Service]] = ZLayer.succeed(new Service {
    private lazy val c = ConfigSource.default.loadOrThrow[AppConfig]
    override def conf: UIO[AppConfig] = UIO(c)
  })
  
}
