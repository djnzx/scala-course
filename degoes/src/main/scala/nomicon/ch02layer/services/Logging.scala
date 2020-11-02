package nomicon.ch02layer.services

import nomicon.ch02layer.Services.Logging
import zio.console.Console
import zio.{UIO, URIO, ZIO, ZLayer}

object Logging {

  /** interface */
  trait Service {
    def info(s: String): UIO[Unit]
    def error(s: String): UIO[Unit]
  }

  /** linking interface to implementation
    * can't fail, so => access */
  def info(s: String): URIO[Logging, Unit] = ZIO.access(_.get.info(s))
  def error(s: String): URIO[Logging, Unit] = ZIO.access(_.get.error(s))

  /** real implementation #1, will be wired further */
  val consoleLogger: ZLayer[Console, Nothing, Logging] = ZLayer.fromFunction((console: Console) =>
    new Service {
      def info(s: String): UIO[Unit] = console.get.putStrLn(s"info - $s")
      def error(s: String): UIO[Unit] = console.get.putStrLn(s"error - $s")
    }
  )
  /** real implementation #2 */
  lazy val fileLogger: ZLayer[Console, Nothing, Logging] = ???
  /** real implementation #3 */
  lazy val elasticLogger: ZLayer[Console, Nothing, Logging] = ???

}
