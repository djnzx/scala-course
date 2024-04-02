package cats101.c099monaderror

import cats.MonadError
import cats.instances.either._

object C099MonadErrorUsage extends App {
  /**
    * in general we produce result or error
    */
  type Result[E, A] = Either[E, A]
  /**
    * we can fix error to String
    * and describe it in terms of one-hole type:
    *
    * whether it's a result with type A
    * whether it's a failure represented as a String
    */
  type ErrorOr[A] = Result[String, A]
  /**
    * and MonadError
    * is an abstraction over this one-hole Wrapper and an error type
    */
  // different ways to extract instance
  val monadError1: MonadError[ErrorOr, String] = implicitly[MonadError[ErrorOr, String]]
  val monadError2: MonadError[ErrorOr, String] = MonadError.apply[ErrorOr, String]
  val monadError: MonadError[ErrorOr, String] = MonadError[ErrorOr, String]

  /** result can be anything */
  val success:  ErrorOr[Int]    = monadError.pure(42)
  val success2: ErrorOr[Double] = monadError.pure(math.Pi)
  /** but error channel is fixed to String for now */
  val failure: ErrorOr[Int] = monadError.raiseError[Int]("badness")
  val failure2: ErrorOr[Int] = monadError.raiseError[Int]("wrong")

  /** can't be partial */
  val handled: ErrorOr[Int] = monadError.handleErrorWith(failure) {
    case "badness" => monadError.pure(13)
    case _ => monadError.raiseError("It's not ok")
  }
  println(handled) // Right(13)

  val handled_magic = monadError.handleErrorWith(failure2) {
    case "magic" => monadError.pure(42)
    case x @ _ => monadError.raiseError(x)
  }
  println(handled_magic) // Left(wrong)


  val validatedWithError: ErrorOr[Int] = monadError.ensure(success)("Number too low!")(_ > 1000)
  println(validatedWithError) // Left(Number too low!)

}
