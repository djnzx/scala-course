package catseffx.timed_input

import cats.effect.{Concurrent, ExitCode, IO, IOApp}
import scala.concurrent.duration._
import scala.io.StdIn

object TimedInputApp5 extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- IO(println("Enter value:")) *> IO(1)
    x <- Concurrent.timeoutTo(IO(StdIn.readLine), 5.seconds, IO("555"))
    _ <- IO(println(s"Value entered: $x"))
  } yield ExitCode.Success

}
