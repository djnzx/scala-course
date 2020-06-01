package monad_nested

import cats.effect.IO

object A1_Problem extends App {

  val total1: IO[Option[Int]] = for {
    o1 <- ioo1       // IO[Option[Int]] `stripped` to Option[Int]
    o2 <- ioo2       // IO[Option[Int]] `stripped` to Option[Int]
  } yield for {
    s1 <- o1         // Option[Int] `stripped` to Int
    s2 <- o2         // Option[Int] `stripped` to Int
  } yield s1 + s2
  println(total1.unsafeRunSync()) // Some(3)

  val total2: IO[Option[Int]] = for {
    o1 <- ioo1       // IO[Option[Int]] `stripped` to Option[Int]
    o2 <- ioo2       // IO[Option[Int]] `stripped` to Option[Int]
  } yield o1.flatMap(v1 => o2.map(v2 => v2 + v1))
  println(total2.unsafeRunSync()) // Some(3)

}
