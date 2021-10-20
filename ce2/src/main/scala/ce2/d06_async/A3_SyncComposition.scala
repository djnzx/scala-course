package ce2.d06_async

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

object A3_SyncComposition extends IOApp {

  /** our API which works in terms of Future */
  trait API {
    def compute: Future[Int] = Future.successful(42)
  }

  /** signature: Either[Throwable, Int] => Unit is an abstraction over Try / Success / Failure
    */
  def doSmth[A](api: API)(implicit ec: ExecutionContext): IO[Int] = {
    IO.async[Int] { cb: (Either[Throwable, Int] => Unit) =>
      api.compute.onComplete {
        case Failure(x) => cb(Left(x))
        case Success(v) => cb(Right(v))
      }

    }.guarantee(IO.shift)
  }

  override def run(args: List[String]): IO[ExitCode] = ???

}
