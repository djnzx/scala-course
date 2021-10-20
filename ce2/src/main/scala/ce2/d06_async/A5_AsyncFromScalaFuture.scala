package ce2.d06_async

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import ce2.common.debug.DebugHelper

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

object A5_AsyncFromScalaFuture extends IOApp {

  /** effect representation */
  val future: () => Future[Int] = () => {
    println("calculating...")
    Future.successful(33)
  }

  /** effect wrapped into IO to suspend */
  val wrapped: IO[Future[Int]] = IO(future())

  def fromScalaFuture[A](iofa: IO[Future[A]])(implicit ec: ExecutionContext): IO[A] =
    iofa.flatMap { fa: Future[A] =>
      IO.async[A] { cb: (Either[Throwable, A] => Unit) =>
        fa.onComplete {
          case Failure(x) => cb(Left(x))
          case Success(a) => cb(Right(a))
        }
      }
    }

  /** remapped into IO without nested stuff */
  val app: IO[Int] = fromScalaFuture(wrapped)(Implicits.global)
  println("==========")

  override def run(args: List[String]): IO[ExitCode] =
    app
      .debug
      .as(ExitCode.Success)
}
