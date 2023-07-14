package lifecycle

import cats.effect.{Deferred, ExitCode, IO, IOApp, Ref, Resource}
import cats.implicits._

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

object Resources2CReleaseInPparallel extends IOApp {

  def log(m: String) = IO(println(m))

  /** resource(s) */
  val resourceA = Resource.make(IO(".") <* log("Resource A acquired"))(_ => log("Resource A released"))
  val resourceB = Resource.make(IO("/") <* log("Resource B acquired"))(_ => log("Resource B released"))

  /** ref to check notification */
  val refIO = Ref[IO].of(true)

  /** signal allow to terminate (graceful cleanup is done) */
  val deferIO = Deferred[IO, Unit]

  def hashes = IO(print("#"))

  def makeCancellable(app: IO[Unit], sleepTime: FiniteDuration) = (ref: Ref[IO, Boolean], d: Deferred[IO, Unit]) => {

    def doIt(ref: Ref[IO, Boolean], d: Deferred[IO, Unit]): IO[Unit] = ref.get.flatMap {
      case true  => app >> IO.sleep(sleepTime) >> doIt(ref, d)
      case false =>
        log(s"Hashes: got a request to cancel. cleaning up...") >>
          d.complete(()) >>
          log(s"Hashes: allowed termination") >>
          IO.unit
    }

    doIt(ref, d).uncancelable
  }

  /** app thread */
  def symbols(chars: String, appName: String, sleepTime: FiniteDuration, ref: Ref[IO, Boolean], d: Deferred[IO, Unit]): IO[Unit] = {

    def doIt(ref: Ref[IO, Boolean], d: Deferred[IO, Unit]): IO[Unit] = ref.get.flatMap {
      case true  =>
        IO(print(chars)) >>
          IO.sleep(sleepTime) >>
          symbols(chars, appName, sleepTime, ref, d)
      case false =>
        log(s"$appName: got a request to cancel. cleaning up...") >>
          d.complete(()) >>
          log(s"$appName: allowed termination") >>
          IO.unit
    }

    doIt(ref, d).uncancelable
  }

  val resources = for {
    a      <- resourceA
    b      <- resourceB
    ref1   <- Resource.eval(refIO)
    ref2   <- Resource.eval(refIO)
    ref3   <- Resource.eval(refIO)
    defer1 <- Resource.eval(deferIO)
    defer2 <- Resource.eval(deferIO)
    defer3 <- Resource.eval(deferIO)
    _      <- Resource.make(IO.unit)(_ => log("finished both. releasing resources..."))
    _      <- Resource.make(IO.unit)(_ =>
                log(s"\nHOOK got SIGINT (^C) got from OS") >>
                  log(s"HOOK sending notification sent for both Apps") >>
                  (ref1.update(_ => false), ref2.update(_ => false), ref3.update(_ => false)).parTupled >>
                  log(s"HOOK waiting from response") >>
                  (defer1.get, defer2.get, defer3.get).parTupled >>
                  log(s"HOOK got response from BOTH apps")
              )
  } yield {
    val app1 = symbols(a, "DOTS", 150.millis, ref1, defer1)
    val app2 = symbols(b, "SLASHES", 500.millis, ref2, defer2)
    val app3 = makeCancellable(hashes, 1.second)(ref3, defer3)
    (app1, app2, app3)
  }

  val app = resources.use { case (app1, app2, app3) =>
    log(s"Started (inside resources)") >> (app1, app2, app3).parTupled
  }

  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
