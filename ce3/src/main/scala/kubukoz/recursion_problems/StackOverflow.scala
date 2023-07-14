package kubukoz.recursion_problems

import cats.effect.IO;

object StackOverflow {

  /** `*>` syntax - is not byName!
    *   - it's by value
    */

  def repeatForever[A](ioa: IO[A]): IO[Unit] =
    ioa *> repeatForever(ioa)

}
