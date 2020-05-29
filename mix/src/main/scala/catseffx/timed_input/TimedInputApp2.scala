package catseffx.timed_input

import cats.effect.{Concurrent, ExitCode, IO, IOApp, Timer}
import cats.syntax.functor._
import cats.syntax.flatMap._
import scala.concurrent.duration._

object TimedInputApp2 extends IOApp {

  def inputWithTimeOut[F[_]](duration: FiniteDuration, default: String)(implicit F: Concurrent[F], T: Timer[F]): F[String] = {
    Concurrent.timeoutTo(F.delay(scala.io.StdIn.readLine), duration, F.pure(default))
  }

  def body[F[_]: Timer](implicit F: Concurrent[F]): F[Unit] = for {
    _ <- F.delay(println("Enter value:"))
    x <- inputWithTimeOut(5.seconds, "555")
    _ <- F.delay(println(s"Value entered: $x"))
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- body[IO]
  } yield ExitCode.Success

}
