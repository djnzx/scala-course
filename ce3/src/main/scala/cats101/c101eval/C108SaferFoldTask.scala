package cats101.c101eval

import cats.Eval

import scala.language.implicitConversions

object C108SaferFoldTask extends App {

  def foldRight[A, B](as: List[A])(acc: B)(f: (A, B) => B): B = as match {
    case Nil => acc
    case h :: t => f(h, foldRight(t)(acc)(f))
  }

  def foldRightEval[A, B](as: List[A])(acc: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = as match {
    case Nil => acc
    case h :: t => Eval.defer { f(h, foldRightEval(t)(acc)(f)) }
  }

  /** lifting value to Eval */
  implicit def accToEval[A](a: => A): Eval[A] = Eval.now(a)

  /** lifting function to Eval */
  implicit def funToEval[A, B](f: (A, B) => B): (A, Eval[B]) => Eval[B] =
    (a, eb) => eb.map { b => f(a, b) }

  /** using standard values, and standard fold functions to call eval */
  val data = List("a", "b", "c")
  val folder: (String, String) => String = (a, acc) => acc + a
  val r: Eval[String] = foldRightEval(data)("")(folder)
  println(r.value)

  implicit def evalToValue[A, A1](a1: A1)(implicit ev: A1 <:< Eval[A]): A = a1.value
  val rs: String = r
  println(rs)
}
