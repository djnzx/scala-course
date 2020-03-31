package cats

import cats.data.Validated
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.validated._
import cats.syntax.either._

object C156ValidatedExtra extends App {
  val v1: Validated[Nothing, Int] = 123.valid.map(_ * 100)
  val v2: Validated[String, Nothing] = "Error".invalid.leftMap(_.toUpperCase)
  val v3: Validated[String, Int] = 123.valid[String].bimap(_ + "!", _ * 100)
  val v4: Validated[String, Int] = "?".invalid[Int].bimap(_ + "!", _ * 100)

  /**
    * We can’t flatMap because Validated isn’t a monad
    */
  val v5: Validated[Nothing, Int] =
    32.valid.andThen { a =>
      10.valid.map { b =>
        a + b }
    }
  val e6: Either[Nothing, Int] = v5.toEither
  val v7 = e6.toValidated
  val v8: Validated[String, Int] = 123.valid[String].ensure("Negative!")(_ > 0)

  val i9: Int = "fail".invalid[Int].getOrElse(0)
  val s10: String = "fail".invalid[Int].fold(_ + "!!!", _.toString)
}
