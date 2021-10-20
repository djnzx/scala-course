package ce2.d03_parallelism

import cats.effect._
import cats.implicits._
import ce2.common.debug.DebugHelper

object ParTraverse extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    tasks
      .parTraverse(task) // <1>
      .debug // <2>
      .as(ExitCode.Success)

  val numTasks = 100
  val tasks: List[Int] = List.range(0, numTasks)

  def task(id: Int): IO[Int] = IO(id).debug // <2>
}
