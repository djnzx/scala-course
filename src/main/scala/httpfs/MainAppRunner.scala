package httpfs

import cats.effect.{ExitCode, IO, IOApp}

object MainAppRunner extends IOApp {

  def run(args: List[String]) =
    AppServer
      .stream[IO]
      .compile
      .drain
      .as(ExitCode.Success)

}
