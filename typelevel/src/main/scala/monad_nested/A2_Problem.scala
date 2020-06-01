package monad_nested

import cats.effect.IO

object A2_Problem extends App {
  // combine two IO[Either[String, Int]]

  val totale: IO[Either[String, Int]] = for {
    e3 <- ioe3  // IO[Either[String, Int]] `stripped` to Either[String, Int]
    e4 <- ioe4  // IO[Either[String, Int]] `stripped` to Either[String, Int]
  } yield for {
    r3 <- e3    // Either[String, Int] `stripped` to Int
    r4 <- e4    // Either[String, Int] `stripped` to Int
  } yield r3 * r4

  println(totale.unsafeRunSync())  // Right(12)

}
