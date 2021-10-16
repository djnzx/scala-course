package d04

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

object CEMyParMap0 extends IOApp {

  def myParMap2[A, B, C](ioa: IO[A], iob: IO[B])(f: (A, B) => C): IO[C] = for {
    fa <- ioa.start
    fb <- iob.start
    _ <- fa.cancel
    ioar <- fa.join.onError(_ => fb.cancel)
    iobr <- fb.join.onError(_ => fa.cancel)
    // TODO: how to deal with Outcome ??? how to handle onCancel and onError
    a <- ioar.fold(???, ???, identity)
    b <- iobr.fold(???, ???, identity)
  } yield f(a, b)

  val app = IO { 1 }

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)

}
