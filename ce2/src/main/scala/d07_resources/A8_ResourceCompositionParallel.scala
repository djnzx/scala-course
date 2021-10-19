package d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.implicits.catsSyntaxTuple2Parallel
import cats.implicits.catsSyntaxTuple2Semigroupal

object A8_ResourceCompositionParallel extends IOApp {

  trait UseCases[A, B] {

    val resA: Resource[IO, A] = ???
    val resB: Resource[IO, B] = ???

    /** sequentially */
    val rc1: Resource[IO, (A, B)] = (resA, resB).tupled

    /** parallel */
    val rc2: Resource[IO, (A, B)] = (resA, resB).parTupled

  }

  override def run(args: List[String]): IO[ExitCode] = ???
}
