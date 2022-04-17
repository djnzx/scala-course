package lifecycle

import cats.effect.ExitCase
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.effect.concurrent.Deferred
import cats.effect.concurrent.Ref
import cats.implicits._

import scala.concurrent.duration.DurationInt

object Resources2App extends IOApp {

  def log(m: String) = IO(println(m))

  val resourceA = Resource.make(IO(5) <* log("Resource A acquired"))(_ => log("Resource A released"))

  /** on Terminate Hook, will be run when somebody send SIGINT */
  def onTerminateHook: Resource[IO, Unit] = Resource.make(IO.unit)(_ => log("2. Resource cleanup"))

  // TODO: propagate cancellation event into app to implement graceful shutdown
  def dots: IO[Unit] = IO(print(".")).uncancelable >> IO.sleep(300.millis) >> dots

  val resources = for {
    a <- resourceA
    _ <- onTerminateHook
  } yield a

  val app = resources.use { a =>
    log(s"using A: $a") >> dots.guarantee(log("\n1. guarantee(...)"))
  }

  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
