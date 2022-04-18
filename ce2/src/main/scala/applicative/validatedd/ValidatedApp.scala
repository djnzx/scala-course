package applicative.validatedd

import cats.data.Validated

/**
  * implicit shortcuts:
  *
  *
  * Control + Option + Shift + `+`
  *
  * Option + Control + Shift + `-`
  */
object ValidatedApp extends App {

  type VV = Validated[List[String], Int]

  val valRight: VV = Validated.valid(33)
  val valLeft: VV = Validated.invalid(List("wrong"))

  def validatePositive(n: Int): VV = Validated.cond(n > 0, n, List("number should be positive"))
  def validateSmall(n: Int): VV = Validated.cond(n < 100, n, List("number should be less than 100"))
  def validateEven(n: Int): VV = Validated.cond(n % 2 == 0, n, List("number should be even"))

  /**
    * requires two monoids
    * for:
    * - List[String]
    * - Int
    */
  def validate(n: Int): VV =
    validatePositive(n)
      .combine(validateSmall(n))
      .combine(validateEven(n))

  println(validate(10))
  println(validate(333))

}
