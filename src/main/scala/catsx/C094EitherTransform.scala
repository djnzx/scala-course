package catsx

import cats.syntax.either._

object C094EitherTransform extends App {
  /**
    * either transformers (combinator)
    *
    * Either and Option are typically used to implement fail-fast error handling
    */
  val x: Int = "Error".asLeft[Int].getOrElse(0)

  val e1: Either[String, Int] = "Error".asLeft[Int]
  val e2: Either[String, Int] = e1.orElse(2.asRight[String])

  // check predicate or produce error
  val e3: Either[String, Int] = (-1).asRight[String].ensure("Must be non-negative!")(_ > 0)

  // becomes right
  val e4: Either[String, Int] = e3.recover {
    case _: String => -1
  }

  val e5: Either[String, Int] = "error".asLeft[Int].recoverWith {
    case str: String => Right(-13)
  }
  println(s"e4 = ${e4}")
  println(s"e5 = ${e5}")

  "foo".asLeft[Int].leftMap(_.reverse)
  6.asRight[String].bimap(_.reverse, _ * 7)

  val sw: Either[Int, String] = 123.asRight[String].swap
}
