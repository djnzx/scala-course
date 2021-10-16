package d05

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import common.debug.DebugHelper

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object ShiftingMultiple extends IOApp {

  def ec(name: String): ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor { r: Runnable =>
      val t = new Thread(r, s"pool-$name-thread-1")
      t.setDaemon(true)
      t
    })

  def run(args: List[String]): IO[ExitCode] =
    (ec("1"), ec("2")) match {
      case (ec1, ec2) =>
        for {
          _ <- IO("one").debug
          _ <- IO.shift(ec1)
          _ <- IO("two").debug
          _ <- IO.shift(ec2)
          _ <- IO("three").debug
        } yield ExitCode.Success
    }

}
