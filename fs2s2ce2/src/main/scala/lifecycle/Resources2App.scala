package lifecycle

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

  /** resource(s) */
  val resourceA: Resource[IO, Int] = Resource.make(IO(5) <* log("Resource A acquired"))(_ => log("Resource A released"))

  /** ref to check notification */
  val refIO: IO[Ref[IO, Boolean]] = Ref[IO].of(true)

  /** signal allow to terminate (graceful cleanup is done) */
  val deferIO: IO[Deferred[IO, Unit]] = Deferred[IO, Unit]

  /** business value */
  val doTheBusiness = IO(print(".")) >> IO.sleep(300.millis)

  /** app */
  def dots(ref: Ref[IO, Boolean], d: Deferred[IO, Unit]): IO[Unit] = ref.get.flatMap {
    case true  => doTheBusiness >> dots(ref, d)
    case false => log("APP: got a request to cancel. cleaning up...") >> d.complete(()) >> log("APP: allowed termination") >> IO.unit
  }.uncancelable

  /** on Terminate Hook, will be run when somebody send SIGINT */
  def terminateHook(ref: Ref[IO, Boolean], d: Deferred[IO, Unit]) =
    Resource.make(IO.unit)(_ =>
      log("\nHOOK: SIGINT (^C) got from OS") >>
        ref.update(_ => false) >>
        log("HOOK: notification sent to App") >>
        d.get >>
        log("HOOK: response got from App")
    )

  val resources = for {
    a     <- resourceA
    ref   <- Resource.eval(refIO)
    defer <- Resource.eval(deferIO)
    _     <- terminateHook(ref, defer)
  } yield {
    val app = dots(ref, defer)
    (a, app)
  }

  val app = resources.use { case (a, app) =>
    log(s"using resource A: $a") >> app
  }

  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
