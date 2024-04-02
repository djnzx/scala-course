package cats101

import cats.Semigroupal
import cats.data.Validated
import cats.instances.list._

object C152Validated extends App {

  type AllErrorsOr[A] = Validated[List[String], A]

  val e12 = Semigroupal[AllErrorsOr].product(
    Validated.invalid(List("Error 1")),
    Validated.invalid(List("Error 2")),
  )

  println(e12)

  /**
    * Either - fail fast
    * Validated - keep all errors
    */
  val o1a: Some[Int]   = Some(1)
  val o1b: Option[Int] = Some(1)
  val o2a: None.type   = None
  val o2b: Option[Int] = None

  val e1a: Right [Nothing, Int]    = Right(123)
  val e1b: Either[Nothing, Int]    = Right(123)
  val e2a: Left  [String, Nothing] = Left("Error")
  val e2b: Either[String, Nothing] = Left("Error")

  // #1. creating via apply, by default
  val v1a: Validated.Valid   [Int]          = Validated.Valid(123)
  val v2a: Validated.Invalid[List[String]]  = Validated.Invalid(List("Fuck"))

  // widen manually to any type
  val v1b: Validated[Nothing, Int]          = Validated.Valid(123)
  val v2b: Validated[List[String], Nothing] = Validated.Invalid(List("Fuck"))

  // #2. widen automatically, because smart constructors
  // in type constructor we need to specify exactly type which widen to
  val v1c: Validated[List[String], Int]     = Validated.valid  [List[String], Int](123)
  val v2c: Validated[List[String], Int]     = Validated.invalid[List[String], Int](List("Badness"))

  // #3. via syntax
  import cats.syntax.validated._
  //                                                    type of the left side
  val v1d: Validated[List[String], Int]     = 123.valid[List[String]]
  //                                                    type of the right side
  val v2d: Validated[List[String], Int]     = List("Shit!").invalid[Int]

  // #4 pure and raiseError respectively
  import cats.syntax.applicative._      // pure
  import cats.syntax.applicativeError._ // raiseError

  type ErrorOr[A] = Validated[List[String], A]
  val v1e: ErrorOr[Int] = 456.pure[ErrorOr]
  val v2e: ErrorOr[Int] = List("Shit!").raiseError[ErrorOr, Int]

  // #5. lifters
  val v2f: Validated[NumberFormatException, Int] = Validated.catchOnly[NumberFormatException]("foo".toInt)
  val v2g: Validated[Throwable, Nothing] = Validated.catchNonFatal(sys.error("Badness"))
  val v2h: Validated[Throwable, Int] = Validated.fromTry(scala.util.Try("foo".toInt))
  val v2i: Validated[String, Int] = Validated.fromEither[String, Int](Left("Badness"))
  val v2j: Validated[String, Int] = Validated.fromOption[String, Int](None, "Badness")

}
