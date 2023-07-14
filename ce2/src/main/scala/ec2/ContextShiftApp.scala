package ec2

import cats.effect.{ExitCode, IO, IOApp}

object ContextShiftApp extends IOApp {

  import CommonParts._
  
  val app: IO[Unit] = for {
    _     <- printLine("Enter path:")
    _     <- IO.shift(BlockingEC)         //shifting to the blocking context
    path  <- readLineBlocking             // run in the the blocking
    lines <- getFileLinesBlocking(path)   // run in the the blocking
              .guarantee(IO.shift(contextShift)) //shifting back must be executed in finalizer
    _     <- printLine(s"File has $lines lines.") 
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    app.as(ExitCode.Success)
}
