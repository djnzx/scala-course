package rtj_ce.part5polymorphic

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Spawn
import cats.effect.kernel.Outcome.Canceled
import cats.effect.kernel.Outcome.Errored
import cats.effect.kernel.Outcome.Succeeded
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import utils._

object Polymorphic2FibersEx extends IOApp.Simple {

  /** Exercise - generalize the following code (race implementation from the Racing lesson) */

  def ioRace[A, B](ioa: IO[A], iob: IO[B]): IO[Either[A, B]] =
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

  def generalRace[F[_], A, B](fa: F[A], fb: F[B])(implicit spawn: Spawn[F]): F[Either[A, B]] =
    spawn.racePair(fa, fb).flatMap {
      case Left((outA, fibB))  =>
        outA match {
          case Succeeded(effectA) => fibB.cancel.flatMap(_ => effectA.map(a => Left(a)))
          case Errored(e)         => fibB.cancel.flatMap(_ => spawn.raiseError(e))
          case Canceled()         =>
            fibB.join.flatMap {
              case Succeeded(effectB) => effectB.map(b => Right(b))
              case Errored(e)         => spawn.raiseError(e)
              case Canceled()         => spawn.raiseError(new RuntimeException("Both computations canceled."))
            }
        }
      case Right((fibA, outB)) =>
        outB match {
          case Succeeded(effectB) => fibA.cancel.flatMap(_ => effectB.map(b => Right(b)))
          case Errored(e)         => fibA.cancel.flatMap(_ => spawn.raiseError(e))
          case Canceled()         =>
            fibA.join.flatMap {
              case Succeeded(effectA) => effectA.map(a => Left(a))
              case Errored(e)         => spawn.raiseError(e)
              case Canceled()         => spawn.raiseError(new RuntimeException("Both computations canceled."))
            }
        }
    }
  // beware this is a simple implementation - certain cases are not taken into account
  // (which would make the code more complicated)

  import scala.concurrent.duration._
  val fast = IO.sleep(1.second) >> IO(42).debug0
  val slow = IO.sleep(2.seconds) >> IO("Scala").debug0
  val race = ioRace(fast, slow)
  val race_v2 = generalRace(fast, slow)

  override def run = race.void
}
