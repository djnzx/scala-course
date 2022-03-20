package course.d1

import cats.effect.IO
import cats.effect.IOApp
import fs2._

object A4Resources extends IOApp.Simple {

  val acquire: IO[Unit] = IO { println("acquiring") }
  val release: IO[Unit] = IO { println("releasing") }
  val err: Stream[IO, INothing] = Stream.raiseError[IO](new Exception("oh noes!"))

  val willFail: Stream[IO, Int] = Stream(1, 2, 3) ++ err

  val app = Stream
    .bracket(acquire)(_ => release)
//    .flatMap(_ => willFail)

  override def run: IO[Unit] =
    app
      .compile
      .drain
}
