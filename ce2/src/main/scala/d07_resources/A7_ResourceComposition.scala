package d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.implicits.catsSyntaxTuple2Semigroupal

object A7_ResourceComposition extends IOApp {

  trait UseCases[A, B, C, D] {

    val resA: Resource[IO, A] = ???
    val resB: Resource[IO, B] = ???

    /** functor */
    val f: A => B = ???
    val rb: Resource[IO, B] = resA.map(f)

    /** applicative */
    val g: (A, B) => C = ???
    val rc: Resource[IO, C] = (resA, resB).mapN(g)

    /** monad */
    val h: A => Resource[IO, D] = ???
    val rd: Resource[IO, D] = resA.flatMap(h)

  }

  override def run(args: List[String]): IO[ExitCode] = ???
}
