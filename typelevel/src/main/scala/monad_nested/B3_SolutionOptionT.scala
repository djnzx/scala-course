package monad_nested

import cats.data.OptionT
import cats.effect.IO

object B3_SolutionOptionT extends App {

  val sumT: OptionT[IO, Int] = for {
    v5 <- OptionT(ioo5)
    v6 <- OptionT(ioo6)
  } yield v5 + v6

  val sum: IO[Option[Int]] = sumT.value
  println(sum.unsafeRunSync())

}
