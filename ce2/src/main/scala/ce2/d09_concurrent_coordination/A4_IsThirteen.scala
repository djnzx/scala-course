package ce2.d09_concurrent_coordination

import cats.effect._
import cats.effect.concurrent._
import cats.implicits._
import ce2.common.debug.DebugHelper

import scala.concurrent.duration._

object A4_IsThirteen extends IOApp {

  def makeRef(x: Long): IO[Ref[IO, Long]] = Ref[IO].of(x)

  def makeDeferred[A]: IO[Deferred[IO, A]] = Deferred[IO, A]

  def beepWhen13(is13: Deferred[IO, Unit]) =
    for {
      _ <- is13.get // Calling get will block the current effect until is13 has a value
      _ <- IO("BEEP!").debug
    } yield ()

  def tickingClock(ticks: Ref[IO, Long], is13: Deferred[IO, Unit]): IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis).debug
      /** update the value */
      count <- ticks.updateAndGet(_ + 1)
      /** check the condition and complete, once condition MET, unblocking Deferred */
      _ <- if (count >= 13) is13.complete(()) else IO.unit // <4>
      _ <- tickingClock(ticks, is13)
    } yield ()

  def run(args: List[String]): IO[ExitCode] =
    for {
      ticks <- makeRef(0)
      is13 <- makeDeferred[Unit] // create a Deferred that will hold a Unit value once the condition is met
      _ <- (
        beepWhen13(is13),
        tickingClock(ticks, is13),
      ).parTupled // two effects are only communicating through the shared `is13` value
    } yield ExitCode.Success

}
