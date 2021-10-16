package d04

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

object NumCpuApp extends IOApp {

  val getNumCpu: IO[Int] = IO { Runtime.getRuntime.availableProcessors() } // 12 on i7 with 6 cores
  def print(n: Int): IO[Unit] = IO { println(n) }

  val app = for {
    n <- getNumCpu
    _ <- print(n)
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
