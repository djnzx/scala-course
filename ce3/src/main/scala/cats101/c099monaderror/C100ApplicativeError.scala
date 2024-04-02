package cats101.c099monaderror

import cats.implicits.catsSyntaxMonadError
import cats101.c099monaderror.C099MonadErrorUsage.{ErrorOr, success}

object C100ApplicativeError extends App {

  import cats.syntax.applicative._
  import cats.syntax.applicativeError._ // raiseError

  // ErrorOr[Int] === Either[String, Int]
  val success3: ErrorOr[Int] = 42.pure[ErrorOr]
  val failure3: ErrorOr[Int] = "Badness".raiseError[ErrorOr, Int]
  val test3: ErrorOr[Int] = success.ensure("Number to low!")(_ > 1000)

  /**
    * further reading
    * https://typelevel.org/cats/api/cats/MonadError.html
    * https://typelevel.org/cats/api/cats/ApplicativeError.html
    */

  import cats.instances.try_._
  import scala.util.Try

  val ex: Throwable = new RuntimeException("went wrong")
  ex.raiseError[Try, Int]

}
