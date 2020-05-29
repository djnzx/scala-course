package way_to_mt

import cats.data.OptionT
import cats.effect.IO

object CombineApp extends App {
  // combine two IO[Option]
  val ioo1: IO[Option[Int]] = IO.pure(Some(1))
  val ioo2: IO[Option[Int]] = IO.pure(Some(2))

  val totalo: IO[Option[Int]] = for {
    o1 <- ioo1 // IO[Option[Int]] `stripped` to Option[Int]
    o2 <- ioo2 // IO[Option[Int]] `stripped` to Option[Int]
  } yield for {
    s1 <- o1   // Option[Int] `stripped` to Int
    s2 <- o2   // Option[Int] `stripped` to Int
  } yield s1 + s2
  println(totalo.unsafeRunSync()) // Some(3)

  val totalOT: OptionT[IO, Int] = for {
    s1 <- OptionT(ioo1)
    s2 <- OptionT(ioo2)
  } yield s1+s2
  val totalOTa: IO[Option[Int]] = totalOT.value

  // combine two IO[Either[String, Int]]
  val ioe3: IO[Either[String, Int]] = IO.pure(Right(3))
  val ioe4: IO[Either[String, Int]] = IO.pure(Right(4))

  val totale: IO[Either[String, Int]] = for {
    e3 <- ioe3  // IO[Either[String, Int]] `stripped` to Either[String, Int]
    e4 <- ioe4  // IO[Either[String, Int]] `stripped` to Either[String, Int]
  } yield for {
    r3 <- e3    // Either[String, Int] `stripped` to Int
    r4 <- e4    // Either[String, Int] `stripped` to Int
  } yield r3 * r4

  println(totale.unsafeRunSync())  // Right(12)

  val total_oe_2: IO[IO[Option[Either[String, Int]]]] =
    for {
      o2 <- ioo2
    } yield for {
      e4 <- ioe4
    } yield for {
      s2 <- o2     // Option
    } yield for {
      r4 <- e4     // Either
    } yield s2 * r4
  val total_oe: IO[Option[Either[String, Int]]] = total_oe_2 flatMap identity
  println(total_oe.unsafeRunSync()) // Some(Right(8))

  val total_eo_2: IO[IO[Either[String, Option[Int]]]] =
    for {
      o2 <- ioo2
    } yield for {
      e4 <- ioe4
    } yield for {
      r4 <- e4     // Either
    } yield for {
      s2 <- o2     // Option
    } yield s2 * r4
  val total_eo: IO[Either[String, Option[Int]]] = total_eo_2 flatMap identity
  val rs8: Either[String, Option[Int]] = total_eo.unsafeRunSync()
  println(rs8) // Right(Some(8))
  rs8.fold(identity, _.toString)




}
