package ce2.d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import ce2.common.debug.DebugHelper

object A3_BasicResourceFailure extends IOApp {

  /** resource definition */
  val resource: Resource[IO, String] = Resource.make(
    IO("> acquiring").debug *> IO("my resource"),
  )(_ => IO("< releasing").debug.void)

  /** resource usage */
  val app = resource
    .use(_ => IO.raiseError(new RuntimeException("oh noes!")))
    .attempt
    .debug

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)

}
