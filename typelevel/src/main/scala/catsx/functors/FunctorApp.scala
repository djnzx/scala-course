package catsx.functors

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Try}

object FunctorApp extends App {

  val inc1: List[Int] = List(1,2,3).map(x => x + 1)
  val inc2: Option[Int] = Some(2).map(x => x + 1)
  val inc3: Try[Int] = Success(33).map(x => x + 1)
  val inc4: Future[Int] = Future.successful(13).map(x => x + 1)

  def do10xList(list: List[Int]): List[Int] = list.map(_ + 1)
  def do10xOption(option: Option[Int]): Option[Int] = option.map(_ + 1)
  def do10xTry(atry: Try[Int]): Try[Int] = atry.map(_ + 1)

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  implicit val listFunctor: Functor[List] = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }

  implicit val tryFunctor: Functor[Try] = new Functor[Try] {
    override def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa.map(f)
  }

  def do10x[F[_]: Functor, A, B](fa: F[A])(f: A => B): F[B] = implicitly[Functor[F]].map(fa)(f)

  sealed trait Tree[+A]
  case class Leaf[+A](value: A) extends Tree[A]
  case class Node[+A](value: A, l: Tree[A], r: Tree[A]) extends Tree[A]
  object Tree {
    def leaf[A](value: A): Tree[A] = Leaf(value)
    def branch[A](value: A, l: Tree[A], r: Tree[A]): Tree[A] = Node(value, l, r)
  }

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Leaf(value)       => Leaf(f(value))
      case Node(value, l, r) => Node(f(value), map(l)(f), map(r)(f))
    }
  }


}
