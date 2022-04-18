package lifecycle

import cats._
import cats.data._
import cats.effect.IO
import cats.effect.Resource
import cats.effect.IOApp
import cats.effect.ExitCode
import cats.effect.concurrent.Ref
import cats.implicits._

import scala.concurrent.duration.DurationInt

object Resources1App extends IOApp {

  def log(m: String) = IO(println(m))

  val resourceA = Resource.make(IO(5) <* log("A acquired"))(_ => log("A released"))
  val resourceB = Resource.make(IO("hello") <* log("B acquired"))(_ => log("B released"))
  val onTerminate = Resource.make(IO.unit)(_ => log("onTerminate handler"))
  val onTerminateRef = Resource.make(Ref[IO].of(IO.unit))(ri =>
    ri.get.flatMap { (cb: IO[Unit]) => log(s"onTerminateREF\nrunning callback given in the body:") >> cb }
  )
  val resources: Resource[IO, (Int, String)] = for {
    a <- resourceA
    b <- resourceB
    _ <- onTerminate
  } yield (a, b)

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
          log("terminated by timeout") // handler will be applied
        ) >>
      log("terminated") // will be printed
  }

  val resources2 = for {
    a   <- resourceA
    b   <- resourceB
    _   <- onTerminate
    ref <- onTerminateRef
  } yield (a, b, ref)

  val app4 = resources2.use { case (a, b, ref) =>
    log(s"using A: $a") >>
      log(s"using B: $b") >>
      log("working with ref...") >> ref.update(_ => IO(println(fansi.Color.Red("trick")))) >>
      IO.never
        .timeoutTo(
          3.seconds, // timeout will raise the error
          log("terminated by timeout") // handler will be applied
        ) >>
      log("terminated") // will be printed
  }

  val app5 = resources.use { case (a, b) =>
    log(s"using A: $a") >>
      log(s"using B: $b") >>
      IO.never >>
      log("terminated")
  }

  override def run(args: List[String]): IO[ExitCode] = app5.as(ExitCode.Success)
}
