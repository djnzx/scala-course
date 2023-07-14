package rtj_cats.eval

import cats._
import cats.implicits._

object EvaluationApp extends App {

  /** around 8000 - stack will blow */
  def factorial(n: Int): Int = n match {
    case 0 => 0
    case n => n + factorial(n - 1)
  }

  def factorialEval(n: Int): Eval[Int] = n match {
    case 0 => Eval.now(0)
    case n => Eval.now(n).flatMap(x => factorialEval(n - 1).map(_ + x))
  }

  // will blow at 8000 approx
  def reverse[A](xs: List[A]): List[A] = xs match {
    case Nil    => Nil
    case h :: t => reverse(t) :+ h
  }

  def reverseEval1[A](xs: List[A]): Eval[List[A]] = xs match {
    case Nil    => Eval.now(Nil)
    case h :: t => Eval.later(()) >> reverseEval1(t).map(_ :+ h)
  }

  def reverseEval2[A](xs: List[A]): Eval[List[A]] = xs match {
    case Nil    => Eval.now(Nil)
    case h :: t => Eval.defer(reverseEval2(t).map(_ :+ h))
  }

//  println(factorialEval(10000).value)

  println(reverseEval1((1 to 10000).toList).value)
  println(reverseEval2((1 to 10000).toList).value)

}
