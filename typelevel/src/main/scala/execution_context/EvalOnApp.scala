package execution_context

import cats.effect.{ExitCode, IO, IOApp}

object EvalOnApp extends IOApp {

  import CommonParts._

  val readPathAndCount: IO[Long] =
    for {
      path <- readLineBlocking
      ls   <- getFileLinesBlocking(path)
    } yield ls

  val app: IO[Unit] = for {
    _     <- printLine("Enter path:")
    lines <- contextShift.evalOn(BlockingEC)(readPathAndCount)
    _     <- printLine(s"File has $lines lines.") 
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)
}
