package cats101

import cats.implicits._

/**
  * https://blog.softwaremill.com/9-tips-about-using-cats-in-scala-you-might-want-to-know-e1bafd365f88
  */
object Cats700 extends App {

  /** 9. Optional lifters */
  val o1a: None.type = None
  //      ^^^^^^^^^      problem
  val o1b: Option[Int] = Option.empty
  val o1c: Option[Int] = none[Int]

  val o2a: Some[Int] = Some(1)
  //      ^^^^^^^^^      problem
  val o2b: Option[Int] = Option(1)
  val o2c: Option[Int] = 1.some

  /** 8. Either lifters */
  //                   ^^^^^^^^^      problem
  val e1a: Left[String, Nothing] = Left("error")
  //                   ^^^^^^^^^      problem
  val e1b: Either[String, Int] = Either.left[String, Int]("error")
  // Scala solution
  val e1c: Either[String, Nothing] = Either.left[String, Nothing]("error")
  // Cats solution
  val e1d: Either[String, Int] = "error".asLeft[Int]

  val success1 = "a".asRight[Int]
  val success2 = "b".asRight[Int]
  val failure = 400.asLeft[String]

  val combined1: Either[Int, String] = success1 *> success2 // right(b)
  val combined2: Either[Int, String] = success2 *> success1 // right(a)
  val combined3: Either[Int, String] = success1 *> failure  // left(400)
  val combined4: Either[Int, String] = failure *> success1  // left(400)
}
