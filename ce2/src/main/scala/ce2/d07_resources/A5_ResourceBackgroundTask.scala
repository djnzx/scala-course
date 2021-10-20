package ce2.d07_resources

import cats.effect._
import cats.implicits._
import ce2.common.debug.DebugHelper

import scala.concurrent.duration._

object A5_ResourceBackgroundTask extends IOApp {

  /** one iteration */
  val iteration: IO[Unit] = IO("looping...").debug *> IO.sleep(100.millis)

  /** infinite loop */
  val loop: IO[Nothing] = iteration.foreverM

  /** fiber as a resource, we start and terminate it exactly here */
  def mkResource(task: IO[Nothing]) = Resource
    .make(
      IO("> forking backgroundTask").debug *> task.start, // here we produce fiber
    ) { fiber => // <3>
      IO("< canceling backgroundTask").debug.void *> fiber.cancel // here we cancel it
    }
    .void // <5>

  val backgroundTask: Resource[IO, Unit] = mkResource(loop)
  // or
  val backgroundTask2: Resource[IO, IO[Nothing]] = loop.background

  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- backgroundTask2.use { _ =>
        /** we ignore parameter because in this case resource is already started in acquire */
        IO.sleep(550.millis) *> IO("decided to terminate").debug // <1>
      }
      _ <- IO("done!").debug
    } yield ExitCode.Success

}
