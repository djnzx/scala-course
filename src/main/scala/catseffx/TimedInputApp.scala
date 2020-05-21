package catseffx

import cats.effect.{Concurrent, ExitCode, IO, IOApp, Timer}
import cats.syntax.flatMap._
import cats.syntax.functor._

import scala.concurrent.duration._

object TimedInputApp extends IOApp {
  def inputWithTimeOut1[F[_]](duration: FiniteDuration, default: String)(implicit F: Concurrent[F], T: Timer[F]): F[String] = {
    val lOrR: F[Either[String, String]] = F.race(
      T.sleep(duration) >> F.pure(default),
      F.delay(scala.io.StdIn.readLine)
    )
    val r: F[String] = lOrR.map { _.fold(identity, identity) }
    r
  }

  def inputWithTimeOut2[F[_]](duration: FiniteDuration, default: String)(implicit F: Concurrent[F], T: Timer[F]): F[String] = {
    Concurrent.timeoutTo(F.delay(scala.io.StdIn.readLine), duration, F.pure(default))
  }

  def body[F[_]: Timer](implicit F: Concurrent[F]): F[Unit] = for {
    _ <- F.delay(println("Enter value:"))
    x <- inputWithTimeOut2(5.seconds, "555")
    _ <- F.delay(println(s"Value entered: $x"))
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- body[IO]
  } yield ExitCode.Success
}
