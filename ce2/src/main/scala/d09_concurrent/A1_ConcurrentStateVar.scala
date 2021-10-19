package d09_concurrent

import cats.effect._
import cats.implicits._
import common.debug.DebugHelper

import scala.concurrent.duration._

object A1_ConcurrentStateVar extends IOApp {

  var ticks: Long = 0L // <2>

  val tickingClock: IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis).debug
      _ = (ticks = ticks + 1) // <3>
      _ <- tickingClock
    } yield ()

  val printTicks: IO[Unit] =
    for {
      _ <- IO.sleep(5.seconds)
      _ <- IO(s"TICKS: $ticks").debug.void // <4>
      _ <- printTicks
    } yield ()

  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- (tickingClock, printTicks).parTupled // <1>
    } yield ExitCode.Success

}
