package catseffx.timed_input

import cats.effect.{Concurrent, ExitCode, IO, IOApp, Timer}
import cats.syntax.functor._
import cats.syntax.flatMap._
import scala.concurrent.duration._

object TimedInputApp1 extends IOApp {

  def inputWithTimeOut[F[_]](duration: FiniteDuration, default: String)(implicit F: Concurrent[F], T: Timer[F]): F[String] = {
    F.race(
      T.sleep(duration) >> F.pure(default),
      F.delay(scala.io.StdIn.readLine)
    ) map { _.fold(identity, identity) }
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
