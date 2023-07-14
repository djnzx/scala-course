package catsx

import cats.Monad

import scala.annotation.tailrec

object C126CustomMonad extends App {

  val optionMonad = new Monad[Option] {

    def flatMap[A, B](opt: Option[A])(fn: A => Option[B]): Option[B] = opt flatMap fn

    def pure[A](opt: A): Option[A] = Some(opt)

    // http://functorial.com/stack-safety-for-free/index.pdf
    @tailrec
    def tailRecM[A, B](a: A)(fn: A => Option[Either[A, B]]): Option[B] =
      fn(a) match {
        case None           => None
        case Some(Left(a1)) => tailRecM(a1)(fn)
        case Some(Right(b)) => Some(b)
      }
  }

}
