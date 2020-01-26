package cats

import cats.implicits._

/**
  * https://blog.softwaremill.com/9-tips-about-using-cats-in-scala-you-might-want-to-know-e1bafd365f88
  */
object Cats700 extends App {
  // 9
  val v1: Option[Int] = 1.some
  val v3: Option[Int] = Some(1)
  val v2: Option[Int] = none

  // 8
  val success1 = "a".asRight[Int]
  val success2 = "b".asRight[Int]
  val failure = 400.asLeft[String]

  val combined1: Either[Int, String] = success1 *> success2 // right(b)
  val combined2: Either[Int, String] = success2 *> success1 // right(a)
  val combined3: Either[Int, String] = success1 *> failure  // left(400)
  val combined4: Either[Int, String] = failure *> success1  // left(400)
}
