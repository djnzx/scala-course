package _udemy.scala_advanced.lectures.part5ts

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Daniel.
  */
object HigherKindedTypes extends App {

  // combine/multiply List(1,2) x List("a", "b") => List(1a, 1b, 2a, 2b)
  // plain approach

  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  def multiply[A, B](optA: Option[A], opyB: Option[B]): Option[(A, B)] =
    for {
      a <- optA
      b <- opyB
    } yield (a, b)

  def multiply[A, B](futA: Future[A], futB: Future[B]): Future[(A, B)] =
    for {
      a <- futA
      b <- futB
    } yield (a, b)

  // use HKT

  trait Monad[F[_], A] { // higher-kinded type class
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  def combine[F[_], A, B](ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] =
    for {
      a <- ma  // flatMap
      b <- mb  // map
    } yield (a, b)

//  val monadList = new MonadList(List(1,2,3))
//  monadList.flatMap(x => List(x, x + 1)) // List[Int]
//  // Monad[List, Int] => List[Int]
//  monadList.map(_ * 2) // List[Int
//  // Monad[List, Int] => List[Int]

  println(combine(List(1,2), List("a", "b")))
  println(combine(Some(2), Some("scala")))
}
