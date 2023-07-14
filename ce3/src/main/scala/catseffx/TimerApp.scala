package catseffx

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration._

object TimerApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = for {
    t0 <- IO.pure(t())
    _  <- IO.sleep(2 seconds)
    _  <- pdtIO(t0)
  } yield ExitCode.Success

}
