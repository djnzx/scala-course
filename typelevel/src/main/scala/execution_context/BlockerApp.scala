package execution_context

import java.nio.file.Paths

import cats.effect.{Blocker, ExitCode, IO, IOApp}

object BlockerApp extends IOApp {

  import CommonParts._
  
  //Blocker.apply returns resource which will automatically close underlying EC
  def app: IO[Unit] = Blocker[IO]
    .use { blocker =>
      for {
        _    <- printLine("Enter path:")
        line <- blocker.blockOn(readLineBlocking)
        path <- IO(Paths.get(line))
        _    <- safeCreate(path)(blocker)
      } yield ()
    }

  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
