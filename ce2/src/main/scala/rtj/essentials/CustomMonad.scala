package rtj.essentials

import scala.annotation.tailrec

object CustomMonad extends App {

  import cats.Monad

  implicit object OptionMonad extends Monad[Option] {
    override def pure[A](x: A): Option[A] = Option(x)
    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)

    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] = f(a) match {
      case None           => None
      case Some(Left(v))  => tailRecM(v)(f)
      case Some(Right(b)) => Some(b)
    }
  }

  type Id[A] = A

  implicit object IdMonad extends Monad[Id] {
    override def pure[A](x: A): Id[A] = x
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)

    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Id[Either[A, B]]): Id[B] = f(a) match {
      case Left(a)  => tailRecM(a)(f)
      case Right(b) => b
    }
  }

  sealed trait Tree[+A]
  final case class Leaf[+A](value: A) extends Tree[A]
  final case class Branch[+A](left: Tree[A], right: Tree[A]) extends Tree[A]

  implicit object TreeMonad extends Monad[Tree] {
    override def pure[A](x: A): Tree[A] = Leaf(x)
    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = fa match {
      case Leaf(value) => f(value)
      case Branch(left, right) =>
        val lb: Tree[B] = flatMap(left)(f)
        val rb: Tree[B] = flatMap(right)(f)
        Branch(lb, rb)
    }

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = ???
  }

}
