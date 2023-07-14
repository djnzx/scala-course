package lifecycle

import cats.effect.kernel.Ref
import cats.effect.{Deferred, ExitCode, IO, IOApp, Resource}
import cats.implicits._

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

object Resources3OwnProtocol extends IOApp {

  def log(m: String) = IO(println(m))

  val resourceA = Resource.make(IO("#") <* log("Resource A acquired"))(_ => log("Resource A released"))

  val refIO = Ref[IO].of(true)
  val deferIO = Deferred[IO, Unit]

  def hashes(char: String): IO[Nothing] = {
    def rec: IO[Nothing] = IO(print(char)) >> IO.sleep(300.millis) >> rec
    rec
  }

  def hashesController(ask: Deferred[IO, Unit], allow: Deferred[IO, Unit]) =
    ask.get >>
      log("proto: got term req. cleaning...") >>
      allow.complete(()) >>
      log("proto: cleanup complete")

  val resources = for {
    r  <- resourceA
    d1 <- Resource.eval(deferIO)
    d2 <- Resource.eval(deferIO)
    _  <- Resource.make(IO.unit)(_ => log("final cleanup..."))
    _  <- Resource.make(IO.unit)(_ =>
            log(s"\nHOOK got SIGINT (^C) got from OS") >>
              log(s"HOOK sending notification to HashesApp") >>
              d1.complete(()) >>
              log(s"HOOK waiting from response") >>
              d2.get >>
              log(s"HOOK got response from BOTH apps")
          )
  } yield (
    hashes(r).uncancelable,
    hashesController(d1, d2).uncancelable
  )

  val app = resources.use { case (app1, app2) =>
    log(s"Started (inside resources)") >> (app1, app2).parTupled
  }

  override def run(args: List[String]): IO[ExitCode] = app.as(ExitCode.Success)
}
