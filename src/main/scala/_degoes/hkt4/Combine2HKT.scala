package _degoes.hkt4

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Combine2HKT extends App {

  // type class
  trait Wrapper[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  // implicit conversion from List[A] to Wrapper[List, A]
  implicit class MList[A](list: List[A]) extends Wrapper[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  // implicit conversion from Option[A] to Wrapper[Option, A]
  implicit class MOption[A](option: Option[A]) extends Wrapper[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  // implicit conversion from Future[A] to Wrapper[Future, A]
  implicit class MFuture[A](future: Future[A]) extends Wrapper[Future, A] {
    override def flatMap[B](f: A => Future[B]): Future[B] = future.flatMap(f)
    override def map[B](f: A => B): Future[B] = future.map(f)
  }

  // ONE implementation
  def combine[F[_], A, B](ma: Wrapper[F, A], mb: Wrapper[F, B]): F[(A, B)] =
    for {
      a <- ma  // flatMap
      b <- mb  // map
    } yield (a, b)
  // F - means the another type

  // usage: each call - call of different function
  println(combine(List(1,2,3), List("a", "b", "c"))) // line 34
  println(combine(Some(2), Some("scala"))) // line 34
  println(Await.result(combine(Future(1000), Future(2000)), 4.seconds)) // line 34
}
