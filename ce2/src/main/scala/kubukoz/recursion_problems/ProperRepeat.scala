package kubukoz.recursion_problems

import cats.effect.IO
import cats.implicits._

object ProperRepeat {

  def usingFlatMap[A](ioa: IO[A]): IO[Nothing] =
    ioa.flatMap(_ => usingFlatMap(ioa))

  def usingDefer[A](ioa: IO[A]): IO[Nothing] =
    ioa *> IO.defer(usingDefer(ioa))

  /** >> is using byName parameter
    */
  def usingLazy[A](ioa: IO[A]): IO[Nothing] =
    ioa >> usingLazy(ioa)

}
