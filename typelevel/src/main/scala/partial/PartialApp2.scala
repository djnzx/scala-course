package partial

import scala.util.Try

/**
  * the power of partial application
  * and handle errors at the very last moment
  */
object PartialApp2 extends App {

  /**
    * our implementation,
    * partial due to nature,
    * kind of http mapping
    * it can brake by nature
    */
  def businessLogic: PartialFunction[Int, String] = {
    case 1 => "one"
    case 2 => throw new IllegalArgumentException("TWO")
    case 10 => "ten"
  }

  /**
    * kind of validation
    * for example http request header checking
    */
  def validate(x: Int) = x < 10

  /**
    * first composition
    * we want to have a validation before processing
    */
  def businessLogicValidated: PartialFunction[Int, String] = {
    case x if businessLogic.isDefinedAt(x) & validate(x) => businessLogic(x)
  }

  /**
    * we try / catch it only if it's definedAt
    * we have:   A => B
    * we build:  A => Either[Throwable, B]
    */
  def catchOnlyIfDefinedAt(f: PartialFunction[Int, String]): PartialFunction[Int, Either[Throwable, String]] = {
    case x if f.isDefinedAt(x) => Try(f(x)).toEither
  }

  /**
    * safe composition,
    * but still partial, ant this is good.
    * we still can deal with that LATER
    */
  val businessLogicValidatedWithExceptionsCaught = catchOnlyIfDefinedAt(businessLogicValidated)

  lazy val a = businessLogicValidatedWithExceptionsCaught(1)  // Right(one), f1 works well
  lazy val b = businessLogicValidatedWithExceptionsCaught(2)  // Left(IllegalArgumentException(TWO)), ex thrown by f1 caught by tryCatch
  lazy val c = businessLogicValidatedWithExceptionsCaught(3)  // MatchError thrown, keep it partial
  lazy val d = businessLogicValidatedWithExceptionsCaught(10) // MatchError thrown, keep it partial due to validation

  /**
    * kind of last hope handler
    * or 404 handler in terms of http
    */
  def takeCareAtTheLastMoment(orElse: String): PartialFunction[Any, Either[Throwable, String]] = {
    case _ => Right(orElse)
  }

  val reallySafeComposition = businessLogicValidatedWithExceptionsCaught orElse takeCareAtTheLastMoment("covered!")

  lazy val x1 = reallySafeComposition(1)     // Right(one),                          implemented in `converter`
  lazy val x2 = reallySafeComposition(2)     // Left(IllegalArgumentException: TWO), implemented in `converter`, but caught later in `catchOnlyIfDefinedAt`
  lazy val x3 = reallySafeComposition(3)     // Right(covered!),                     hasn't implemented, but handled in `takeCareAtTheLastMoment`
  lazy val x10 = reallySafeComposition(10)   // Right(covered!),                     has filtered, hence implemented => hasn't implemented, but handled in `takeCareAtTheLastMoment`

  println(x1)
  println(x2)
  println(x3)
  println(x10)
}
