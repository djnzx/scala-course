package mortals

import scalaz._
import Scalaz._
import pprint.{pprintln => println}

object Mortals002a extends App {

  import scala.reflect.runtime.universe._
  val a, b, c = Option(1)
  val r: String = show { reify { 
    for { i <- a; j <- b; k <- c} yield (i + j + k) 
  } }
  println(r)

  val x1: Option[Int] =
    for {
      i: Int <- a
    } yield i

  /**
    * a match {
    *   case (i @ (_: Int)) => true
    *   case _ => false
    * }
    */

  val x2 = a.map { (i: Int) => i }
  /**
    * https://github.com/oleg-py/better-monadic-for
    */

  trait ForComprehensible[C[_]] {
    def map[A, B](f: A => B): C[B]
    def flatMap[A, B](f: A => C[B]): C[B]
    def withFilter[A](p: A => Boolean): C[A]
    def foreach[A](f: A => Unit): Unit
  }

  import scala.concurrent._
  import scala.concurrent.ExecutionContext.Implicits.global

  // sequential
  for {
    i <- Future { 123 }
    j <- Future { 234 }
  } yield i + j
  
  // parallel
  val f1 = Future { 123 }
  val f2 = Future { 234 } 
  val f3: Future[Int] = for {i <- f1; j <- f2 } yield i + j

  val io1 = Right(Option(123))
  val io2 = Right(Option(234))
  /**
    * plain:
    */
  val r1: Either[Nothing, Option[Int]]  = for {
    o1 <- io1
    o2 <- io2
  } yield for {
    i1 <- o1
    i2 <- o2
  } yield i1 + i2
  /**
    * Monad transformers
    */
  val r2: Right[Nothing, Option[Int]] = (for {
    i1 <- OptionT(io1)
    i2 <- OptionT(io2)
  } yield i1 + i2).run
  val t = Future { Some(123) } // where is .liftM ???
  
}
