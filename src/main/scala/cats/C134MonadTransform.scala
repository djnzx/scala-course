package cats

import cats.data.OptionT
import cats.instances.either._
import cats.syntax.applicative._

object C134MonadTransform extends App {

  type ErrorOr[A] = Either[String, A]
  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  val aa: ErrorOrOption[Int] = 30.pure[ErrorOrOption]
  val bb = 12.pure[ErrorOrOption]

  val c: OptionT[ErrorOr, Int] = for {
    a <- aa
    b <- bb
  } yield a + b

}
