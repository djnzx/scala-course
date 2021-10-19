package d06_async

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

object A2_Design extends IOApp {

  def syncSum(l: Int, r: Int): IO[Int] =
    IO.async[Int] { cb: (Either[Throwable, Int] => Unit) =>
      cb(Right(l + r))
    }

  override def run(args: List[String]): IO[ExitCode] = ???

}
