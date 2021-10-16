package d04

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.catsSyntaxTuple2Semigroupal

object CEMyParMap extends IOApp {

  def myParMap2[A, B, C](ioa: IO[A], iob: IO[B])(f: (A, B) => C): IO[C] = for {
    p <- IO.racePair(ioa, iob)
    r = p match {
      case Left((a, fb))  => ??? //(IO.pure(a), fb.join).mapN(f)
      case Right((fa, b)) => ??? //(fa.join, IO.pure(b)).mapN(f)
    }
  } yield r

  val app = IO { 1 }

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)

}
