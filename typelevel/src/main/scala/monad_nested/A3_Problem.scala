package monad_nested

import cats.effect.IO

/**
  * order matters
  */
object A3_Problem extends App {
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
