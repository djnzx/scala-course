package lifecycle

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.effect.concurrent.Deferred
import cats.effect.concurrent.Ref
import cats.implicits._

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

object Resources2AResourceHook extends IOApp {

  def log(m: String) = IO(println(m))

  /** resource(s) */
  val resourceA: Resource[IO, String] = Resource.make(IO(".") <* log("Resource A acquired"))(_ => log("Resource A released"))
  val resourceB: Resource[IO, String] = Resource.make(IO("/") <* log("Resource B acquired"))(_ => log("Resource B released"))

  /** ref to check notification */
  val ref1IO: IO[Ref[IO, Boolean]] = Ref[IO].of(true)
  val ref2IO: IO[Ref[IO, Boolean]] = Ref[IO].of(true)

  /** signal allow to terminate (graceful cleanup is done) */
  val defer1IO: IO[Deferred[IO, Unit]] = Deferred[IO, Unit]
  val defer2IO: IO[Deferred[IO, Unit]] = Deferred[IO, Unit]

  /** app thread */
  def symbols(chars: String, appName: String, sleepTime: FiniteDuration, ref: Ref[IO, Boolean], d: Deferred[IO, Unit]): IO[Unit] = {

    def doIt(ref: Ref[IO, Boolean], d: Deferred[IO, Unit]): IO[Unit] = ref.get.flatMap {
      case true => IO(print(chars)) >> IO.sleep(sleepTime) >> symbols(chars, appName, sleepTime, ref, d)
      case false =>
        log(s"$appName: got a request to cancel. cleaning up...") >> d.complete(()) >> log(s"$appName: allowed termination") >> IO.unit
    }

    doIt(ref, d).uncancelable
  }

  def guaranteeIO(appName: String, ref: Ref[IO, Boolean], d: Deferred[IO, Unit]) =
    log(s"\nHOOK for $appName: SIGINT (^C) got from OS") >>
      ref.update(_ => false) >>
      log(s"HOOK for $appName: notification sent to App") >>
      d.get >>
      log(s"HOOK for $appName: response got from App")

  /** on Terminate Hook, will be run when somebody send SIGINT */
  def terminateHook(appName: String, ref: Ref[IO, Boolean], d: Deferred[IO, Unit]) =
    Resource.make(IO.unit)(_ => guaranteeIO(appName, ref, d))

  val resources = for {
    a      <- resourceA
    b      <- resourceB
    ref1   <- Resource.eval(ref1IO)
    ref2   <- Resource.eval(ref2IO)
    defer1 <- Resource.eval(defer1IO)
    defer2 <- Resource.eval(defer2IO)
    _      <- terminateHook("DOTS", ref1, defer1)
    _      <- terminateHook("SLASHES", ref2, defer2)
  } yield {
    val app1 = symbols(a, "DOTS", 200.millis, ref1, defer1)
    val app2 = symbols(b, "SLASHES", 500.millis, ref2, defer2)
    (app1, app2)
  }

  val app = resources.use { case (app1, app2) =>
    log(s"Starting...") >> (app1, app2).parTupled
  }

  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
