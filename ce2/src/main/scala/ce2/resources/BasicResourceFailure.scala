package ce2.resources

import cats.effect._
import ce2.common.debug.DebugHelper

object BasicResourceFailure extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    stringResource
      .use(_ => IO.raiseError(new RuntimeException("oh noes!"))) // <1>
      .attempt
      .debug
      .as(ExitCode.Success)

  val stringResource: Resource[IO, String] =
    Resource.make(
      IO("> acquiring stringResource").debug *> IO("String"),
    )(_ => IO("< releasing stringResource").debug.void)
}
