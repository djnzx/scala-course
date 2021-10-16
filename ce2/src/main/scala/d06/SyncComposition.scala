package d06

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

object SyncComposition extends IOApp {

  type PlainCallback[A] = Either[Throwable, A] => Unit

  type Callback[A] = PlainCallback[A] => Unit

  def async[A](k: Callback[A]): IO[A] = ???

  def syncSum(l: Int, r: Int): IO[Int] =
    IO.async { cb =>
      cb(Right(l + r))
    }

  trait API {
    def compute: Future[Int] = Future.successful(42)
  }

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
