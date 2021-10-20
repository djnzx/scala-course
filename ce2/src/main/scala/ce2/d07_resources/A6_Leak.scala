package ce2.d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

object A6_Leak extends IOApp {

  def leaky[A, B](ia: IO[A], ib: IO[B]): IO[(A, B)] = for {
    fiberA <- ia.start // !!!
    fiberB <- ib.start
    a <- fiberA.join // !!!
    b <- fiberB.join
  } yield (a, b)

  /** If ia raises an error, fiberA.join will fail and fiberB will still be allocated and running
    */

  override def run(args: List[String]): IO[ExitCode] = ???
}
