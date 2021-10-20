package d09_concurrent

import cats.effect._
import cats.implicits._
import common.debug.DebugHelper

import scala.concurrent.duration._

object A6_IsThirteenLatch extends IOApp {

  def beeper(latch: CountdownLatch) =
    for {
      _ <- latch.await
      _ <- IO("BEEP!").debug
    } yield ()

  def tickingClock(latch: CountdownLatch): IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis).debug
      _ <- latch.decrement
      _ <- tickingClock(latch)
    } yield ()

  def run(args: List[String]): IO[ExitCode] =
    for {
      latch <- CountdownLatch(13)
      _ <- (beeper(latch), tickingClock(latch)).parTupled
    } yield ExitCode.Success

}
