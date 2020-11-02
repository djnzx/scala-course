package nomicon.ch02layer

import zio._
import Services.Logging
import zio.console.Console

object Logging {
  
  trait Service {
    def info(s: String): UIO[Unit]
    def error(s: String): UIO[Unit]
  }

  def info(s: String): URIO[Logging, Unit] = ZIO.accessM(_.get.info(s))
  def error(s: String): URIO[Logging, Unit] = ZIO.accessM(_.get.error(s))

  val consoleLogger: ZLayer[Console, Nothing, Logging] = ZLayer.fromFunction(console =>
    new Service {
      def info(s: String): UIO[Unit] = console.get.putStrLn(s"info - $s")
      def error(s: String): UIO[Unit] = console.get.putStrLn(s"error - $s")
    }
  )
  lazy val elasticLogger: ZLayer[Console, Nothing, Logging] = ???
  lazy val fileLogger: ZLayer[Console, Nothing, Logging] = ???

}
