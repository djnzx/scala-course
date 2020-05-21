package catseffx.timed_input

import cats.effect.{Concurrent, ContextShift, IO, Sync, Timer}
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.applicative._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.io.StdIn

object TimedInputApp4 extends App {

  def printLine[F[_]](s: String)(implicit F: Sync[F]): F[Unit] = F.delay(println(s))
  def readLine[F[_]](implicit F: Sync[F]): F[String] = F.delay(StdIn.readLine)

  def app[F[_]: Timer](implicit F: Concurrent[F]): F[Unit] = for {
    _ <- printLine("Enter value:")
    x <- Concurrent.timeoutTo(readLine, 5.seconds, "555".pure[F])
    _ <- printLine(s"Value entered: $x")
  } yield ()

  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)
  implicit val context: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  app[IO].unsafeRunSync()
}
