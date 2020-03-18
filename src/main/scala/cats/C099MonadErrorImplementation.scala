package cats

object C099MonadErrorImplementation extends App {

  /**
    * MonadError is typed Monad with F[E]
    * F - monad wrapper type
    * E - error type
    */
  import cats.instances.either._
  import cats.syntax.either._ // ensure

  /**
    * the semantic is:
    * whether we have an error with type String
    * whether we have a result with type A
    */
  type ErrorOr[A] = Either[String, A]

  // different ways to extract instance
  val monadError1: MonadError[ErrorOr, String] = implicitly[MonadError[ErrorOr, String]]
  val monadError2: MonadError[ErrorOr, String] = MonadError.apply[ErrorOr, String]
  val monadError:  MonadError[ErrorOr, String] = MonadError[ErrorOr, String]

  val success: ErrorOr[Int]     = monadError.pure(42)
  val failure: ErrorOr[Nothing] = monadError.raiseError("Badness")

  // what's the idea of double wrapping ?
  val handled: ErrorOr[ErrorOr[String]] = monadError.handleError(failure) {
    case "Badness" => monadError.pure("It's ok")
    case _         => monadError.raiseError("It's not ok")
  }
  println(handled) // Right(Right(It's ok))

  val validatedWithError: ErrorOr[Int] = monadError.ensure(success)("Number too low!")(_ > 1000)
  println(validatedWithError) // Left(Number too low!)

  import cats.syntax.applicative._      // pure
  import cats.syntax.applicativeError._ // raiseError
//  import cats.syntax.monadError._

  // ErrorOr[Int] === Either[String, Int]
  val success3: ErrorOr[Int] = 42.pure[ErrorOr]
  val failure3: ErrorOr[Int] = "Badness".raiseError[ErrorOr, Int]
  val test3   : ErrorOr[Int] = success.ensure("Number to low!")(_ > 1000)
  /**
    * further reading
    * https://typelevel.org/cats/api/cats/MonadError.html
    * https://typelevel.org/cats/api/cats/ApplicativeError.html
    */

  import scala.util.Try
  import cats.instances.try_._

  val ex: Throwable = new RuntimeException("went wrong")
  ex.raiseError[Try, Int]
}
