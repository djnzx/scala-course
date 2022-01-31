package rtj.part4coordination

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Ref
import cats.syntax.parallel._
import utils._

import scala.concurrent.duration._

object C1RefsExercises extends IOApp.Simple {

  /** Exercise */
  def tickingClockImpure(): IO[Unit] = {
    var ticks: Long = 0L

    def tickingClock: IO[Unit] = for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis()).debug
      _ <- IO(ticks += 1) // not thread safe
      _ <- tickingClock
    } yield ()

    def printTicks: IO[Unit] = for {
      _ <- IO.sleep(5.seconds)
      _ <- IO(s"TICKS: $ticks").debug
      _ <- printTicks
    } yield ()

    for {
      _ <- (tickingClock, printTicks).parTupled
    } yield ()
  }

  def tickingClockPure(): IO[Unit] = {
    def tickingClock(ticks: Ref[IO, Int]): IO[Unit] = for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis()).debug
      _ <- ticks.update(_ + 1) // thread safe effect
      _ <- tickingClock(ticks)
    } yield ()

    def printTicks(ticks: Ref[IO, Int]): IO[Unit] = for {
      _ <- IO.sleep(5.seconds)
      t <- ticks.get
      _ <- IO(s"TICKS: $t").debug
      _ <- printTicks(ticks)
    } yield ()

    for {
      tickRef <- Ref[IO].of(0)
      _ <- (tickingClock(tickRef), printTicks(tickRef)).parTupled
    } yield ()
  }

  def tickingClockWeird(): IO[Unit] = {
    val ticks = Ref[IO].of(0) // IO[ref] - every time gets NEW EMPTY CLEAN Ref[Int]

    def tickingClock: IO[Unit] = for {
      t <- ticks // ticks will give you a NEW Ref
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis()).debug
      _ <- t.update(_ + 1) // thread safe effect
      _ <- tickingClock
    } yield ()

    def printTicks: IO[Unit] = for {
      t <- ticks // ticks will give you a NEW Ref
      _ <- IO.sleep(5.seconds)
      currentTicks <- t.get
      _ <- IO(s"TICKS: $currentTicks").debug
      _ <- printTicks
    } yield ()

    for {
      _ <- (tickingClock, printTicks).parTupled
    } yield ()
  }

  override def run =
//    tickingClockImpure()
    tickingClockPure()
//      tickingClockWeird()
}
