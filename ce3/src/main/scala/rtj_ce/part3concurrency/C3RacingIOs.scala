package rtj_ce.part3concurrency

import cats.effect.Fiber
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.kernel.Outcome
import cats.effect.kernel.Outcome.Canceled
import cats.effect.kernel.Outcome.Errored
import cats.effect.kernel.Outcome.Succeeded
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import utils._

object C3RacingIOs extends IOApp.Simple {

  val meaningOfLife = runWithSleep(42, 1.second)
  val favLang = runWithSleep("Scala", 2.seconds)

  def runWithSleep[A](value: A, duration: FiniteDuration): IO[A] =
    (
      IO(s"starting computation: $value").debug0 >>
        IO.sleep(duration) >>
        IO(s"computation for $value: done") >>
        IO(value)
    ).onCancel(IO(s"computation CANCELED for $value").debug0.void)

  def testRace() = {
    val first: IO[Either[Int, String]] = IO.race(meaningOfLife, favLang)
    /*
      - both IOs run on separate fibers
      - the first one to finish will complete the result
      - the loser will be canceled
     */

    first.flatMap {
      case Left(mol)   => IO(s"Meaning of life won: $mol")
      case Right(lang) => IO(s"Fav language won: $lang")
    }
  }

  def testRacePair() = {
    val raceResult: IO[Either[
      (Outcome[IO, Throwable, Int], Fiber[IO, Throwable, String]), // (winner result, loser fiber)
      (Fiber[IO, Throwable, Int], Outcome[IO, Throwable, String]), // (loser fiber, winner result)
    ]] = IO.racePair(meaningOfLife, favLang)

    raceResult.flatMap {
      case Left((outMol, fibLang))  => fibLang.cancel >> IO("MOL won").debug0 >> IO(outMol).debug0
      case Right((fibMol, outLang)) => fibMol.cancel >> IO("Language won").debug0 >> IO(outLang).debug0
    }
  }

  /** Exercises: 1 - implement a timeout pattern with race 2 - a method to return a LOSING effect from a race (hint: use
    * racePair) 3 - implement race in terms of racePair
    */
  // 1
  def timeout[A](io: IO[A], duration: FiniteDuration): IO[A] = {
    val timeoutEffect = IO.sleep(duration)
    val result = IO.race(io, timeoutEffect)

    result.flatMap {
      case Left(v)  => IO(v)
      case Right(_) => IO.raiseError(new RuntimeException("Computation timed out."))
    }
  }

  val importantTask = IO.sleep(2.seconds) >> IO(42).debug0
  val testTimeout = timeout(importantTask, 1.seconds)
  val testTimeout_v2 = importantTask.timeout(1.seconds)

  // 2
  def raceInverted[A, B](ioa: IO[A], iob: IO[B]): IO[Either[A, B]] =
    IO.racePair(ioa, iob).flatMap {
      case Left((_, fibB))  =>
        fibB.join.flatMap {
          case Succeeded(resultEffect) => resultEffect.map(result => Right(result))
          case Errored(e)              => IO.raiseError(e)
          case Canceled()              => IO.raiseError(new RuntimeException("Loser canceled."))
        }
      case Right((fibA, _)) =>
        fibA.join.flatMap {
          case Succeeded(resultEffect) => resultEffect.map(result => Left(result))
          case Errored(e)              => IO.raiseError(e)
          case Canceled()              => IO.raiseError(new RuntimeException("Loser canceled."))
        }
    }

  // 3
  def simpleRace[A, B](ioa: IO[A], iob: IO[B]): IO[Either[A, B]] =
    IO.racePair(ioa, iob).flatMap {
      case Left((outA, fibB))  =>
        outA match {
          case Succeeded(effectA) => fibB.cancel >> effectA.map(a => Left(a))
          case Errored(e)         => fibB.cancel >> IO.raiseError(e)
          case Canceled()         =>
            fibB.join.flatMap {
              case Succeeded(effectB) => effectB.map(b => Right(b))
              case Errored(e)         => IO.raiseError(e)
              case Canceled()         => IO.raiseError(new RuntimeException("Both computations canceled."))
            }
        }
      case Right((fibA, outB)) =>
        outB match {
          case Succeeded(effectB) => fibA.cancel >> effectB.map(b => Right(b))
          case Errored(e)         => fibA.cancel >> IO.raiseError(e)
          case Canceled()         =>
            fibA.join.flatMap {
              case Succeeded(effectA) => effectA.map(a => Left(a))
              case Errored(e)         => IO.raiseError(e)
              case Canceled()         => IO.raiseError(new RuntimeException("Both computations canceled."))
            }
        }
    }

//  override def run = testRace().debug.void
  override def run = raceInverted(meaningOfLife, favLang).debug0.void
}
