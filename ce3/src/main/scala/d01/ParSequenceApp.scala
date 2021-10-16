package d01

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.catsSyntaxParallelSequence1
import d01.debug.DebugHelper

object ParSequenceApp extends IOApp {

  /** one unit of job */
  def job(n: Int): IO[Int] = IO { n }.debug

  /** source data */
  val tasks: List[IO[Int]] = List.tabulate(20)(job)

  /** combination */
  val app: IO[List[Int]] =
    tasks
      .parSequence
      .debug

  /** entry point */
  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
