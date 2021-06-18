package nomicon.ch06

import zio._
import zio.clock.Clock
import zio.console.Console
import zio.duration._

object DotsApp extends App {
  
  val dot = console.putStr(".")
  val dotAndSleep = dot *> clock.sleep(100 millis)
  def dots: ZIO[Clock with Console, Nothing, Nothing] = dotAndSleep *> dots
  
  def app: ZIO[Clock with Console, Nothing, Unit] = for {
    fiber <- dots.fork
    _    <- clock.sleep(5 seconds)
    _    <- fiber.interrupt
  } yield ()

  val app2 = dots.timeout(5 seconds)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app2.exitCode
}
