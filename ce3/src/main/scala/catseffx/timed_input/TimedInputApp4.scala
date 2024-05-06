package catseffx.timed_input

import cats.Applicative
import cats.effect._
import cats.effect.implicits.genTemporalOps_
import cats.implicits._

import scala.concurrent.duration._
import scala.io.StdIn

// https://typelevel.org/cats-effect/docs/faq
object TimedInputApp4 extends IOApp.Simple {

  def printLine[F[_]](s: String)(implicit F: Sync[F]): F[Unit] = F.blocking(println(s))
  def readLine[F[_]](implicit F: Async[F]): F[String]          = F.cancelable(F.blocking(StdIn.readLine), ().pure[F])

  def app[F[_]: Applicative: Async] = for {
    _ <- printLine("Enter value:")
    x <- readLine.timeoutTo(1.second, "555".pure[F])
    _ <- printLine(s"Value entered: $x")
  } yield ()

  override def run = app[IO]

}
