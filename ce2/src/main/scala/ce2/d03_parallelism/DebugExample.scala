package ce2.d03_parallelism

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._
import ce2.common.debug.DebugHelper

object DebugExample extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    seq.as(ExitCode.Success)

  val hello = IO("hello").debug // <1>
  val world = IO("world").debug // <1>

  val seq =
    (hello, world)
      .mapN((h, w) => s"$h $w")
      .debug // <1>

}
