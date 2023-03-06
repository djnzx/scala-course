package ec

import cats.effect.kernel.Outcome
import cats.effect.{Async, ExitCode, FiberIO, IO, IOApp}

object ECExperiments extends IOApp {

  private def tn = Thread.currentThread.getName

  val a = IO.delay(println(tn))    // io-compute-4
  val b = IO.blocking(println(tn)) // io-compute-blocker-4

  val fiber: IO[FiberIO[Unit]] = a.start
  val outcome: IO[Outcome[IO, Throwable, Unit]] = fiber.flatMap(_.join)
  outcome.map {
    case Outcome.Succeeded(fa) => ???
    case Outcome.Errored(e) => ???
    case Outcome.Canceled() => ???
  }



  override def run(args: List[String]): IO[ExitCode] =
    IO.println(computeWorkerThreadCount) >>
      a >>
      b as ExitCode.Success
}
