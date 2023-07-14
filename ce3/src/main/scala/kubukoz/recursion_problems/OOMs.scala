package kubukoz.recursion_problems

import cats.effect.IO
import com.kubukoz.DebugUtils;

object OOMs {

  def repeatBad[A](ioa: IO[A]): IO[Unit] =
    for {
      _ <- ioa
      _ <- repeatBad(ioa)
    } yield ()

  def repeatBadExplained[A](ioa: IO[A]): IO[Unit] = DebugUtils.withDesugar {
    for {
      _ <- ioa
      _ <- repeatBadExplained(ioa)
    } yield ()
  }

  // approach1 : betterMonadicFor

}
