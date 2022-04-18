package httpfs2

import cats.effect.{ExitCode, IO, IOApp}

object AppRunner extends IOApp {

  def run(args: List[String]) =
    AppServer
      .stream[IO]
      .compile
      .drain
      .as(ExitCode.Success)

}
