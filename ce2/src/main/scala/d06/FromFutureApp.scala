package d06

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import common.debug.DebugHelper

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

object FromFutureApp extends IOApp {

  /** effect representation */
  val future: () => Future[Int] = () => {
    println("calculating...")
    Future.successful(33)
  }

  /** effect wrapped into IO */
  val wrapped: IO[Future[Int]] = IO(future())

  def fromFuture[A](iofa: IO[Future[A]])(implicit ec: ExecutionContext): IO[A] =
    iofa.flatMap { fa: Future[A] =>
      IO.async[A] { cb: (Either[Throwable, A] => Unit) =>
        fa.onComplete {
          case Failure(x) => cb(Left(x))
          case Success(a) => cb(Right(a))
        }
      }
    }

  /** remapped into IO without nested stuff */
  val ioa: IO[Int] = fromFuture(wrapped)(Implicits.global)
  println("==========")

  val app = ioa.debug
  println("==========")

  override def run(args: List[String]): IO[ExitCode] =
    app
      .as(ExitCode.Success)
}
