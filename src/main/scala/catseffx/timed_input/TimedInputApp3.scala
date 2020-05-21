package catseffx.timed_input

import cats.effect.{Concurrent, ExitCode, IO, IOApp, Timer}
import cats.syntax.functor._
import cats.syntax.flatMap._
import scala.concurrent.duration._

object TimedInputApp3 extends IOApp {

  def body[F[_]: Timer](implicit F: Concurrent[F]): F[Unit] = for {
    _ <- F.delay(println("Enter value:"))
    x <- Concurrent.timeoutTo(F.delay(scala.io.StdIn.readLine), 5.seconds, F.pure("555"))
    _ <- F.delay(println(s"Value entered: $x"))
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- body[IO]
  } yield ExitCode.Success

}
