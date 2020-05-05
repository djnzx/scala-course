package fp_red.exercises.c04errorhandling

import scala.annotation.tailrec
import scala.{Either => _, Left => _, Option => _, Right => _, _}
// hide std library `Option` and `Either`, since we are writing our own in this chapter

sealed trait Either[+E,+A] {
  def map[B](f: A => B): Either[E, B] = this match {
    case Right(a) => Right(f(a))
    case Left(e)  => Left(e)
  }
  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
    case Right(a) => f(a)
    case Left(e)  => Left(e)
  }
  def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = this match {
    case Right(a) => Right(a)
    case Left(_)  => b
  }
  // plain version
  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] =
    this match {
      case Right(a) => b match {
        case Right(b) => Right(f(a,b))
        case Left(ee) => Left(ee)
      }
      case Left(e)  => Left(e)
  }
  // flatMap syntax
  def map2fm[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] =
    this flatMap(ar => b map { br => f(ar, br) })
  // for comprehension syntax
  def map2fx[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] =
    for {
      ar <- this
      br <- b
    } yield f(ar,br)
}

case class Left[+E](get: E) extends Either[E,Nothing]
case class Right[+A](get: A) extends Either[Nothing,A]

object Either extends App {

  def traverse[E,A,B](es: List[A])(f: A => Either[E, B]): Either[E, List[B]] = es match {
    case Nil  => Right(Nil) // exit from recursion
    case h::t => f(h).map2( traverse(t)(f) )((x: B, xs: List[B]) => x :: xs )
  }

  def traverseTR[E,A,B](es: List[A])(f: A => Either[E, B]): Either[E, List[B]] = {
    @tailrec
    def go(esx: List[A], acc: List[B]): Either[E, List[B]] = esx match {
      case Nil  => Right(acc)   // finished successfully
      case h::t => f(h) match { // decomposing and processing
        case Left(e)  => Left(e)       // quit if error
        case Right(b) => go(t, b::acc) // add in reverse order to make it bullet-fast
      }
    }
    go(es, Nil) map { _ reverse } // reverse order to make it corresponding original one
  }

  // it looks like this is flatten
  def sequence[E,A](es: List[Either[E,A]]): Either[E,List[A]] = es match {
    case Nil     => Right(Nil) // exit from recursion
    case ::(h,t) => h.map2( sequence(t) )(_ :: _)
  }

  def flatten[E,A](es: List[Either[E,A]]): Either[E,List[A]] = {
    @tailrec
    def go(tail: List[Either[E,A]], acc: List[A]): Either[E,List[A]] = tail match {
      case Nil  => Right(acc)
      case h::t => h match {
        case Left(e)  => Left(e)
        case Right(a) => go(t, a :: acc)
      }
    }
    go(es, Nil) map { _ reverse }
  }

  println("traverse test")
  val fae1: Int => Either[String, Int] = (a: Int) => Right(a+1)

  val fae2: Int => Either[String, Int] = (a: Int) => a match {
    case 3 => Left("3:(")
    case _ => Right(a + 1)
  }
  val fae3: Int => Either[String, Int] = (a: Int) =>
    if (a != 3) Right(a + 1)
    else        Left("3...")

  val esa = List(1,2,3,4)
  val r1: Either[String, List[Int]] = traverseTR(esa)(fae1)
  val r2: Either[String, List[Int]] = traverseTR(esa)(fae2)
  println(esa)
  println(r1)
  println(r2)
  println("sequence test")

  val el1 = List(Right(1), Right(2), Right(3))
  val el2 = List(Right(1), Right(2), Left("E"))
  val rs1: Either[String, List[Int]] = flatten(el1)
  val rs2: Either[String, List[Int]] = flatten(el2)
  println(el1)
  println(el2)
  println(rs1)
  println(rs2)

  def mean(xs: IndexedSeq[Double]): Either[String, Double] =
    if (xs.isEmpty)
      Left("mean of empty list!")
    else
      Right(xs.sum / xs.length)

  def safeDiv(x: Int, y: Int): Either[Exception, Int] =
    try Right(x / y)
    catch { case e: Exception => Left(e) }

  def Try[A](a: => A): Either[Exception, A] =
    try Right(a)
    catch { case e: Exception => Left(e) }

}
