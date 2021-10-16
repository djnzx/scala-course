package d01

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.catsSyntaxTuple2Parallel
import d01.debug.DebugHelper

object ParMapErrors extends IOApp {

  val ok = IO("hi").debug
  val ex1 = IO.raiseError[String](new RuntimeException("oh!")).debug
  val ex2 = IO.raiseError[String](new RuntimeException("noes!")).debug

  def combine[A, B] = (a: A, b: B) => (a, b)
  val e0 = (ok, ok).parMapN(combine)
  val e1 = (ok, ex1).parMapN(combine)
  val e2 = (ex1, ok).parMapN(combine)
  val e3: IO[(String, String)] = (ex2, ex1).parMapN(combine)
  val e3t: IO[(String, String)] = (ex2, ex1).parTupled
  val e3v1: IO[Unit] = (ex2, ex1).parMapN(combine).void
  val e3v2: IO[Unit] = (ex2, ex1).parTupled.void

  def run(args: List[String]): IO[ExitCode] = {
    /* attempt lifts any exception to IO[Either[Throwable, A]] */
    e0.attempt.debug *> // hi, hi => Right(hi, hi)
      IO("---").debug *>
      e1.attempt.debug *> // hi, ex => Left(ex)
      IO("---").debug *>
      e2.attempt.debug *> // hi, ex => Left(ex)
      IO("---").debug *>
      e3.attempt.debug *> // stops after first fail
      IO.pure(ExitCode.Success)
  }

}
