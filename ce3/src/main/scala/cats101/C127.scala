package cats101

import cats.Monad

import scala.annotation.tailrec
import cats.syntax.flatMap._
import cats.syntax.functor._

object C127 extends App {

  sealed trait Tree[+A]
  final case class Leaf[A](value: A) extends Tree[A]
  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  def leaf[A](a: A): Tree[A] = Leaf(a)
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  implicit val treeMonad: Monad[Tree] = new Monad[Tree] {
    override def pure[A](x: A): Tree[A] = leaf(x)

    override def flatMap[A, B](tree: Tree[A])(f: A => Tree[B]): Tree[B] = tree match {
      case Leaf(value) => f(value)
      case Branch(left, right) => {
        val l2: Tree[B] = flatMap(left)(f)
        val r2: Tree[B] = flatMap(right)(f)
        branch(l2, r2)
      }
    }

    def tailRecM2[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = flatMap(f(a)) {
      case Left(value) => tailRecM(value)(f) // still A - flatMap it
      case Right(value) => leaf(value)       // already B - leaf
    }

    // https://stackoverflow.com/questions/44504790/cats-non-tail-recursive-tailrecm-method-for-monads
    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {

      @tailrec
      def go(open: List[Tree[Either[A, B]]], closed: List[Option[Tree[B]]]): List[Tree[B]] =
        open match {
          // leaf already B - move to closed (acc)
          case Leaf(Right(v)) :: next => go(next, Some(pure(v)) :: closed)
          // leaf type A - convert
          case Leaf(Left(v)) :: next => go(f(v) :: next, closed)
          // if branch - unpack this branch to list and process further
          case Branch(l, r) :: next => go(l :: r :: next, None :: closed)
          // conversion is done. List needs to be composed to Tree[B]
          case Nil => closed.foldLeft(Nil: List[Tree[B]]) { (acc: List[Tree[B]], maybeTree: Option[Tree[B]]) =>
            maybeTree.map(t => t :: acc).getOrElse {
              val left :: right :: tail = acc
              branch(left, right) :: tail
            }
          }
        }

      go(List(f(a)), Nil).head
    }
  }

  branch(leaf(100), leaf(200))
    .flatMap(x => branch(leaf(x - 1), leaf(x + 1)))

  for {
    a <- branch(leaf(100), leaf(200))       // import cats.syntax.flatMap._
    b <- branch(leaf(a - 10), leaf(a + 10)) // import cats.syntax.flatMap._
    c <- branch(leaf(b - 1), leaf(b + 1))   // import cats.syntax.functor._
  } yield c

}
