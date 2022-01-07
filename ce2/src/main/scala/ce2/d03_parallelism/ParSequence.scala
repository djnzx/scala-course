package ce2.d03_parallelism

import cats.effect._
import cats.implicits._
import ce2.common.debug.DebugHelper

object ParSequence extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    tasks
      .parSequence // <1>
      .debug // <2>
      .as(ExitCode.Success)

  val numTasks = 100
  val tasks: List[IO[Int]] = List.tabulate(numTasks)(task)

  def task(id: Int): IO[Int] = IO(id).debug // <2>
}
