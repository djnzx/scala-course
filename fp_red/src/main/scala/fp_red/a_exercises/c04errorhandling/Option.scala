package fp_red.a_exercises.c04errorhandling

import scala.annotation.tailrec
import scala.{Either => _, Option => _, Some => _, _} // hide std library `Option`, `Some` and `Either`, since we are writing our own in this chapter

sealed trait Option[+A] {
  def map[B](f: A => B): Option[B] = this match {
    case None    => None
    case Some(a) => Some(f(a))
  }

  def getOrElse[B >: A](default: => B): B = this match {
    case None    => default
    case Some(a) => a
  }

  def orElse[B >: A](ob: => Option[B]): Option[B] = this match {
    case None    => ob
    case Some(a) => Some(a)
  }

  def orElse3[B >: A](ob: => Option[B]): Option[B] = this match {
    case None    => ob
    case Some(_) => this
  }

  def orElse2[B >: A](ob: => Option[B]): Option[B] = map (Some(_)) getOrElse ob

  def flatMap[B](f: A => Option[B]): Option[B] = this match {
    case None    => None
    case Some(a) => f(a)
  }

  def flatMap2[B](f: A => Option[B]): Option[B] = map(f) getOrElse None

  def filter(f: A => Boolean): Option[A] = this match {
    case Some(a) if f(a) => this
    case _               => None
  }
  def filter2(f: A => Boolean): Option[A] = flatMap(a => if (f(a)) Some(a) else None)

}
case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]

object Option extends App {
  def failingFn(i: Int): Int = {
    val y: Int = throw new Exception("fail!") // `val y: Int = ...` declares `y` as having type `Int`, and sets it equal to the right hand side of the `=`.
    try {
      val x = 42 + 5
      x + y
    }
    catch { case e: Exception => 43 } // A `catch` block is just a pattern matching block like the ones we've seen. `case e: Exception` is a pattern that matches any `Exception`, and it binds this value to the identifier `e`. The match returns the value 43.
  }

  def failingFn2(i: Int): Int = {
    try {
      val x = 42 + 5
      x + ((throw new Exception("fail!")): Int) // A thrown Exception can be given any type; here we're annotating it with the type `Int`
    }
    catch { case e: Exception => 43 }
  }

  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  // EXERCISE 2: Implement the variance function
  // (if the mean is m , variance is the mean of
  // math.pow(x - m, 2) , see definition ) in terms of mean and flatMap .

  val dist = (x: Double, mean: Double) => Math.pow(x - mean, 2)

  def variance(xs: Seq[Double]): Option[Double] = for {
    avg <- mean(xs)
    ds = xs.map(x => dist(x, avg))
    res <- mean(ds)
  } yield res
//  println(variance(List(1,2)))

  def map2[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = for {
    av <- a
    bv <- b
    c = f(av, bv)
  } yield c

  def map2b[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a.flatMap(av => b.map(bv => f(av, bv)))

  def sequenceTR[A](oas: List[Option[A]]): Option[List[A]] = {
    @tailrec
    def go(oast: List[Option[A]], acc: List[A]): Option[List[A]] = oast match {
      case Nil => Some(acc)
      case h::t => h match {
        case None => None
        case Some(a) => go(t, a::acc)
      }
    }
    go(oas, Nil) map { _ reverse }
  }

  def sequenceR[A](oas: List[Option[A]]): Option[List[A]] = oas match {
    case Nil =>  Some(Nil)
    case h::t => map2(h, sequenceR(t))((x: A, xs: List[A])=> x :: xs)
  }

  val sequence = sequenceR[Int] _
//  val sequence = sequenceTR[Int] _

  val lo = (1 to 5).map(Some(_)).toList
  println(sequence(Nil))       // Some(List())
  println(sequence(lo))        // Some(List(1, 2, 3, 4, 5))
  println(sequence(lo:+None))  // None
  println(sequence(None::lo))  // None

  def traverseTR[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = {
    @tailrec
    def go(ast: List[A], acc: List[B]): Option[List[B]] = ast match {
      case Nil  => Some(acc)
      case h::t => f(h) match {
        case Some(b) => go(t, b :: acc)
        case _       => None
      }
    }
    go(as, Nil) map { _ reverse }
  }

  def traverseR[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = as match {
    case Nil  => Some(Nil)
    case h::t => map2(f(h), traverseR(t)(f))((x: B, xs: List[B])=> x :: xs)
  }

  val sl = List(1,2,3,4,5)
  val f1 = (x: Int) => Some(x * 10)
  val f2 = (x: Int) => x match {
    case 3 => None
    case _ => Some(x * 10)
  }

  println(traverseTR(sl)(f1)) // Some(List(10, 20, 30, 40, 50))
  println(traverseTR(sl)(f2)) // None
  println(traverseR(sl)(f1)) // Some(List(10, 20, 30, 40, 50))
  println(traverseR(sl)(f2)) // None
}
