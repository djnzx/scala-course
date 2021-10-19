package d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import common.debug.DebugHelper

object A2_BasicResource extends IOApp {

  /** resource definition */
  val resource: Resource[IO, String] = Resource.make(
    IO("> acquiring").debug *> IO("my resource"),
  )(_ => IO("< releasing").debug.void)

  /** resource usage */
  val app = resource
    .use { s =>
      IO(s"$s got!").debug
    }

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)

}
