package catsx

import cats.syntax.either._

object C092Either extends App {

  val a: Either[Nothing, Int] = 3.asRight
  val b: Either[String, Int] = 5.asRight[String]

  val c: Either[String, Int] = for {
    x <- a
    y <- b
  } yield x*x + y*y

  def sumIfNonNegatives(xs: List[Int]): Either[String, Int] =
    xs.foldLeft(Right(0): Either[String, Int])(
      //                ^^^^^^^^^^^^^^^^^^^^^
      (acc, el) => if (el > 0) acc.map(_ + el) else Either.left("Negative found")
    )

  def sumIfNonNegatives2(xs: List[Int]): Either[String, Int] =
    xs.foldLeft(0.asRight[String])(
      //          ^^^^^^^^^^^^^^^
      (acc, el) => if (el > 0) acc.map(_ + el) else Either.left("Negative found")
    )

  println(sumIfNonNegatives(List(2,3,4)))
  println(sumIfNonNegatives(List(2,-3,4)))

  val e1: Either[NumberFormatException, Int] = Either.catchOnly[NumberFormatException]("foo".toInt)
  val e2: Either[Throwable, Nothing] = Either.catchNonFatal(sys.error("Badness"))
  println(e1)
  println(e2)
}
