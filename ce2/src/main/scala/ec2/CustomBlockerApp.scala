package ec2

import cats.effect.{Blocker, ExitCode, IO, IOApp}

object CustomBlockerApp extends IOApp {

  import CommonParts._

  def displayNameWithBlocker(blocker: Blocker): IO[Unit] =
    for {
      _    <- printLine("Enter your name:")
      name <- blocker.blockOn(readLineBlocking)
      _    <- printLine(s"Hello, $name")
    } yield ()

  override def run(args: List[String]): IO[ExitCode] = {
    val blocker = Blocker.liftExecutionContext(customBlockingEC)

    displayNameWithBlocker(blocker)
      .as(ExitCode.Success)
      .guarantee(IO(customBlockingEC.shutdownNow()))
  }

}
