package catsx

import cats.Functor
import cats.syntax.functor._     // map

object C060Tree extends App {
  sealed trait Tree[+A]

  val func = (n: Int) => n * 2

  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  final case class Leaf[A](value: A) extends Tree[A]

  implicit val tree_functor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Leaf(value)         => Leaf(f(value))
      case Branch(left, right) => Branch(map(left)(f), map(right)(f) )
    }
  }

  val t: Tree[Int] = Branch(
    Leaf(1),
    Branch(
      Branch(
        Leaf(2),
        Leaf(3)
      ),
      Branch(
        Leaf(4),
        Leaf(5)
      )
    )
  )

  val t2 = t.map(func)
  println(t)
  println(t2)
}
