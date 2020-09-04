package mortals

import scalaz._
import Scalaz._

import scala.concurrent.Future

object Mortals001a {
  
  trait TerminalSync {
    def read(): String
    def write(t: String): Unit
  }
  trait TerminalAsync {
    def read(): Future[String]
    def write(t: String): Future[Unit]
  }

  trait Foo[C[_]] {
    def create(i: Int): C[Int]
  }

  object FooList extends Foo[List] {
    def create(i: Int): List[Int] = List(i)
  }

  type EitherString[T] = Either[String, T]
  
  object FooEitherString extends Foo[EitherString] {
    def create(i: Int): Either[String, Int] = Right(i)
  }
  object FooEitherString2 extends Foo[Either[String, *]] {
    def create(i: Int): Either[String, Int] = Right(i)
  }
  
  type Id[T] = T

  object FooId extends Foo[Id] {
    def create(i: Int): Int = i
  }

  val x1: List[Int]           = FooList        .create(123)
  val x2: Either[String, Int] = FooEitherString.create(234)
  val x3: Int                 = FooId          .create(345)

  trait Terminal[C[_]] {
    def read: C[String]
    def write(t: String): C[Unit]
  }
  
  type Now[X] = X
  
  object TerminalSync extends Terminal[Now] {
    def read: String = ???
    def write(t: String): Unit = ???
  }

  object TerminalAsync extends Terminal[Future] {
    def read: Future[String] = ???
    def write(t: String): Future[Unit] = ???
  }

  trait Execution[C[_]] {
    def chain[A, B](c: C[A])(f: A => C[B]): C[B] // flatMap
    def create[A](b: A): C[A]                    // unit
  }

  def echo[C[_]](t: Terminal[C], e: Execution[C]): C[String] =
    e.chain(t.read) { in: String =>
      e.chain(t.write(in)) { _: Unit => 
        e.create(in)
      }
    }
    
//  object Execution {
    implicit class Ops[A, C[_]](c: C[A]) {
      def flatMap[B](f: A => C[B])(implicit e: Execution[C]): C[B] = e.chain(c)(f)
      def map[B](f: A => B)(implicit e: Execution[C]): C[B] = e.chain(c)(f andThen e.create)
    }
//  }

  def echo2[C[_]](implicit t: Terminal[C], e: Execution[C]): C[String] =
    t.read.flatMap { in: String =>
      t.write(in).map { _: Unit =>
        in
      }
    }

  def echo3[C[_]](implicit t: Terminal[C], e: Execution[C]): C[String] =
    for {
      in <- t.read
      _  <- t.write(in)
    } yield in

  val futureEcho: Future[String] = echo[Future](???, ???)
  val nowEcho: Now[String] = echo[Now](???, ???)
  /**
    * the problem with Future:
    * it mixes description & running
    */
}
