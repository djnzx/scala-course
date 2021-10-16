package d01

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.catsSyntaxParallelTraverse1
import d01.debug.DebugHelper

object ParTraverseApp extends IOApp {

  /** source data */
  val tasks: List[Int] = (1 to 10).toList

  /** one unit of job */
  def job(n: Int): IO[Int] = IO { n }.debug

  /** combination */
  val app: IO[List[Int]] =
    tasks
      .parTraverse(job) // actually it's the same as parMapN
      .debug

  /** entry point */
  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
