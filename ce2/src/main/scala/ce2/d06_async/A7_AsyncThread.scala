package ce2.d06_async

import cats.effect._
import ce2.common.debug.DebugHelper

import scala.concurrent.ExecutionContext

object A7_AsyncThread extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO("on default context").debug
      _ <- effect.debug
      _ <- IO("where am I?").debug
    } yield ExitCode.Success

  val effect: IO[String] =
    IO.async { cb =>
      ExecutionContext.global.execute {
        new Runnable {
          def run() = ???
        }
      }
    }
}
