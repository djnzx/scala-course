package d09_concurrent

import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import common.debug.DebugHelper

import scala.concurrent.duration._

object A2_ConcurrentStateRef extends IOApp {

  def createMVar(x: Long): IO[Ref[IO, Long]] = Ref[IO].of(x)

  def tickingClock(ticks: Ref[IO, Long]): IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis).debug
      _ <- ticks.update(_ + 1) // <4>
      _ <- tickingClock(ticks)
    } yield ()

  def printTicks(ticks: Ref[IO, Long]): IO[Unit] =
    for {
      _ <- IO.sleep(5.seconds)
      n <- ticks.get // <5>
      _ <- IO(s"TICKS: $n").debug
      _ <- printTicks(ticks)
    } yield ()

  def run(args: List[String]): IO[ExitCode] =
    for {
      ticks <- createMVar(0L) // <2>
      _ <- (tickingClock(ticks), printTicks(ticks)).parTupled // <3>
    } yield ExitCode.Success

}
