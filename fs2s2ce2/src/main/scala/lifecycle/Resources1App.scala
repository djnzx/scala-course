package lifecycle

import cats._
import cats.data._
import cats.effect.IO
import cats.effect.Resource
import cats.effect.IOApp
import cats.effect.ExitCode
import cats.implicits._

import scala.concurrent.duration.DurationInt

object Resources1App extends IOApp {

  def log(m: String) = IO(println(m))

  val resourceA = Resource.make(IO(5) <* log("A acquired"))(_ => log("A released"))
  val resourceB = Resource.make(IO("hello") <* log("B acquired"))(_ => log("B released"))
  val onTerminate = Resource.make(IO.unit)(_ => log("on terminate handler"))

  val resources: Resource[IO, (Int, String)] = for {
    a     <- resourceA
    bbbbb <- resourceB
    _     <- onTerminate
  } yield (a, bbbbb)

  val app1 = resources.use { case (a, b) =>
    log(s"using A: $a") >>
      log(s"using B: $b") >>
      IO.never >>
      log("terminated") // will never run, since SIGINT breaks this chain
  }

  val app2 = resources.use { case (a, b) =>
    log(s"using A: $a") >>
      log(s"using B: $b") >>
      IO.never
        .timeout(5.seconds) >> // will raise exception
      log("terminated") // will never run, since normal flow is broken
  }

  val app3 = resources.use { case (a, b) =>
    log(s"using A: $a") >>
      log(s"using B: $b") >>
      IO.never
        .timeoutTo(
          5.seconds, // timeout will raise the error
          log("terminated by timeout"), // handler will be applied
        ) >>
      log("terminated") // will be printed
  }

  override def run(args: List[String]): IO[ExitCode] = app3.as(ExitCode.Success)
}
