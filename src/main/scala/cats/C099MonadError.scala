package cats

object C099MonadError extends App {

  /**
    * MonadError is typed Monad with F[E]
    * F - monad type
    * E - error type
    */
  trait MonadError0[F[_], E] extends Monad[F] {

    // Lift an error into the `F` context:
    def raiseError[A](e: E): F[A]

    // Handle an error, potentially recovering from it:
    def handleError[A](fa: F[A])(f: E => A): F[A]

    // Test an instance of `F`,
    // failing if the predicate is not satisfied:
    def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
  }

  import cats.MonadError
  import cats.instances.either._
  import cats.syntax.either._

  type ErrorOr[A] = Either[String, A]

  val monadError: MonadError[ErrorOr, String] = MonadError[ErrorOr, String]

  val success: ErrorOr[Int] = monadError.pure(42)
  val failure: ErrorOr[Nothing] = monadError.raiseError("Badness")

  val handled: ErrorOr[ErrorOr[String]] = monadError.handleError(failure) {
    case "Badness" => monadError.pure("It's ok")
    case _         => monadError.raiseError("It's not ok")
  }

  monadError.ensure(success)("Number too low!")(_ > 1000)

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
}
