package rtj_cats

import cats._
import cats.data._
import cats.implicits._

object Semi extends App {

  val la = List(1, 2, 3)
  val lb = List("a", "b")

  val lc: List[(Int, String)] = la zip lb
  val lc2: List[(Option[Int], Option[String])] = la.map(Some(_)) zipAll (lb.map(Some(_)), None, None)
//  pprint.pprintln(lc2)

  val r1 = Semigroup[String].combine("ABC", "xyz")
  val r2 = Semigroup[List[String]].combine("ABC".pure[List], "xyz".pure[List])
//  pprint.pprintln(r1)
//  pprint.pprintln(r2)

  import cats.data.Validated
  type ErrorsOr[T] = Validated[List[String], T]
  val validatedSemigroupal = Semigroupal[ErrorsOr] // requires the implicit Semigroup[List[_]]
  val invalidsCombination = validatedSemigroupal.product(
    Validated.invalid(List("Something wrong", "something else wrong")),
    Validated.invalid(List("This can't be right")) // combine due to applicative
  )

  type EitherErrorsOr[T] = Either[List[String], T]
  import cats.instances.either._ // implicit Monad[Either]
  val eitherSemigroupal = Semigroupal[EitherErrorsOr]
  val eitherCombination = eitherSemigroupal.product( // in terms of map/flatMap
    Left(List("Something wrong", "something else wrong")),
    Left(List("This can't be right")) // doesn't propagate due to a monadic semantic
  )

  pprint.pprintln(invalidsCombination)
  pprint.pprintln(eitherCombination)

}
