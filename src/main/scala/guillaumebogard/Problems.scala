package guillaumebogard

trait Problems {
  /**
    * problem #1
    */
  def computeDiscountedPrice(originalPrice: Double, discountPercent: Double): Double =
    if (discountPercent > 75) {
      throw new RuntimeException("Can't apply discount")
    } else {
      originalPrice - (originalPrice * discountPercent / 100)
    }
  /**
    * solution #1A
    */
  def computeDiscountedPrice1A(originalPrice: Double, discountPercent: Double): Option[Double] =
    if (discountPercent > 75) None
    else Some(originalPrice - (originalPrice * discountPercent / 100))
  /**
    * solution #2A
    */
  def computeDiscountedPrice2A(originalPrice: Double, discountPercent: Double): Either[String, Double] =
    if (discountPercent > 75) Left("Can't apply discount")
    else Right(originalPrice - (originalPrice * discountPercent / 100))
  /**
    * solution #2B
    */
  def computeDiscountedPrice2B(originalPrice: Double, discountPercent: Double): Either[String, Double] =
    Either.cond(
      // predicate
      discountPercent <= 75,
      // true - right
      originalPrice - (originalPrice * discountPercent / 100),
      // false - left
      "Can't apply discount"
    )
  /**
    * solution #2C
    */
  sealed trait BusinessCase
  final case object CantApplyDiscount extends BusinessCase
  def computeDiscountedPrice2C(originalPrice: Double, discountPercent: Double): Either[BusinessCase, Double] =
    Either.cond(
      // predicate
      discountPercent <= 75,
      // true - right
      originalPrice - (originalPrice * discountPercent / 100),
      // false - left
      CantApplyDiscount
    )

  /**
    * A value of type IO[User] is the representation of
    * a likely impure program that has been turned into
    * a referentially transparent value by suspending its execution.
    *
    * merely the blueprint of a program waiting to be explicitly ran.
    */
  def getUser(id: Int): cats.effect.IO[String]

  import cats.effect.IO
  import cats.syntax.flatMap._

  val a: IO[Unit] = IO(println("- Hello there"))
  val b: IO[Unit] = IO(println("- General Kenobi!"))
  val c1: IO[Unit] = a *> b  // IO
  val c2: IO[Unit] = a <* b  // IO
  val c3: IO[Unit] = a >> b  // cats.flatMap syntax

  val failedIO: IO[Int] = IO.raiseError(new NumberFormatException("Boom!"))
  // it will be wrapped in the interpreter during execution
  // actually - fold
  val recoveredIO: IO[String] = failedIO.redeemWith({
    case e: NumberFormatException => IO { println("Something went wrong", e); "42" }
  }, i => IO(i.toString) )

  // never ever do that !!!!
  def getUser: IO[String] = throw new Exception("No user found")

}
