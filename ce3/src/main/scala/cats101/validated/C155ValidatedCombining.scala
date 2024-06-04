package cats101.validated

import cats.Semigroupal
import cats.data.Validated
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.validated._

object C155ValidatedCombining extends App {

  type AllErrorsOr[A] = Validated[List[String], A]

  val s1: Semigroupal[AllErrorsOr] = Semigroupal[AllErrorsOr]

  /**
    * left side combined by joining list.
    * right side combined automatically because of
    * Monoid[List[String]] instance
    * by joining all the params to the tuple
    */
  val s2: Validated[List[String], (Int, Int, Int)] = (
    List("Err #1").invalid[Int],
    List("Err #2").invalid[Int],
    123.valid[List[String]]
  ).tupled

  println(s2)

  import cats.data.NonEmptyVector
  (
  NonEmptyVector.of("Error 1").invalid[Int],
  NonEmptyVector.of("Error 2").invalid[Int]
  ).tupled
}
