package cats

import cats.instances.list._
import cats.instances.option._
import cats.instances.int._

import scala.annotation.tailrec

object C168Foldable extends App {
  val data = List(1, 2, 3, 4)

  val f: Foldable[List] = Foldable[List]
  val sum: Int = f.foldLeft(data, 0)(_ + _)
  val sum1: Int = f.fold(data) // folding via monoid

  val o1: Option[Int] = Option(1)
  val o2: Option[Int] = Option.empty

  val o1f: String = Foldable[Option].foldLeft(o1, "Folding: ")((acc, a) => acc + a.toString)
  val o2f: String = Foldable[Option].foldLeft(o2, "Folding: ")((acc, a) => acc + a.toString)

  /**
    * `reverse` can be easily implemented
    * by using TAIL RECURSION and accumulator
    * complexity: O(N)
    */
  def reverse[A](as: List[A]): List[A] = {
    @tailrec
    def doReverse(tail: List[A], acc: List[A]): List[A] = tail match {
      case Nil => acc
      case h :: t => doReverse(t, h :: acc)
    }

    doReverse(as, Nil)
  }

  val datar = reverse(data) // List(4,3,2,1)

  /**
    * `traverse` can be easily implemented
    * by using NON TAIL RECURSION and accumulator
    * complexity: O(N)
    */
  def traverse[A](as: List[A])(f: A => A): List[A] = as match {
    case Nil => Nil
    case h :: t => f(h) :: traverse(t)(f)
  }

  /**
    * `foldLeft` can be easily implemented
    * by using TAIL RECURSION
    * complexity: O(N)
    */
  @tailrec
  def foldLeft[A, B](xs: List[A], acc: B)(f: (B, A) => B): B = xs match {
    case Nil => acc
    case h :: t => foldLeft(t, f(acc, h))(f)
  }

  println(foldLeft(data, 0)(_ + _))
  println(foldLeft(data, 0)(_ - _))

  /**
    * foldRight TAIL RECURSION
    * via reverse + foldLeft
    * complexity: O(2*N)
    */
  def foldRight1[A, B](xs: List[A], acc: B)(f: (B, A) => B): B = {
    val xsr = reverse(xs)
    foldLeft(xsr, acc)(f)
  }

  /**
    * foldRight NON TAIL RECURSION
    * complexity: O(N)
    */
  def foldRight2[A, B](xs: List[A], acc: B)(f: (B, A) => B): B = xs match {
    case Nil => acc
    case h :: t => f(foldRight2(t, acc)(f), h)
  }

  /**
    * we can implement foldRight
    * complexity: O(N)
    * and still stack friendly
    * by using Eval
    */
  def foldRight3[A, B](xs: List[A], eacc: Eval[B])(f: (Eval[B], A) => Eval[B]): Eval[B] = xs match {
    case Nil => eacc
//  case h :: t =>                  f(foldRight2(t,  acc)(f), h)
    case h :: t => eacc.flatMap(_ => f(foldRight3(t, eacc)(f), h))
  }

  // type aliases
  type FoldFn     [B, A]=  (     B,  A) =>      B
  type EvalFoldFn [B, A] = (Eval[B], A) => Eval[B]
  // original `fold` function (B, A) => B
  val foldFn1: (String, Int) => String = (s: String, a: Int) => s"$s : $a"
  val foldFn2: (String, Int) => String = (s,         a     ) => s"$s : $a"
  val foldFn3                          = (s: String, a: Int) => s"$s : $a"
  val foldFn4: FoldFn[String, Int]     = (s: String, a: Int) => s"$s : $a"
  val foldFn5: FoldFn[String, Int]     = (s,         a     ) => s"$s : $a"
  val foldFn6                          = (s: String, a: Int) => s"$s : $a"
  // rewritten `fold` function (Eval[B], A) => Eval[B]
  val evalFoldFn1: (Eval[String], Int) => Eval[String] = (es: Eval[String], a: Int) => es.map(s => s"$s : $a")
  val evalFoldFn2: (Eval[String], Int) => Eval[String] = (es              , a     ) => es.map(s => s"$s : $a")
  val evalFoldFn3                                      = (es: Eval[String], a: Int) => es.map(s => s"$s : $a")
  // with type aliases
  val evalFoldFn4: EvalFoldFn[String, Int]             = (es: Eval[String], a: Int) => es.map(s => s"$s : $a")
  val evalFoldFn5: EvalFoldFn[String, Int]             = (es              , a     ) => es.map(s => s"$s : $a")
  // convert
  def convert1[B, A](f: FoldFn[B, A]): EvalFoldFn[B, A] = (eb, a) => eb.map(b => f(b, a))
  def convert2[B, A](f: (B, A) => B ): EvalFoldFn[B, A] = (eb, a) => eb.map(b => f(b, a))
  // converting original function
  val evalFoldFn6: EvalFoldFn[String, Int]             = (eb, a) => eb.map(b => foldFn1(b, a))
  val evalFoldFn7: EvalFoldFn[String, Int]             = convert1(foldFn1)
  val evalFoldFn8                                      = convert1(foldFn1: FoldFn[String, Int])
  val evalFoldFn9                                      = convert1(foldFn4)
  val evalFoldFn10                                     = convert2(foldFn4)

  val fr1r: String = foldRight1(data, "")(foldFn1)
  val fr2r: String = foldRight2(data, "")(foldFn2)
  val fr3r: String = foldRight3(data, Eval.now(""))(evalFoldFn7).value

  val fr4r: String = Foldable[List].foldRight(data, Eval.now(""))((a, eb) => eb.map(b => s"$b : $a")).value
  println(fr1r)
  println(fr2r)
  println(fr3r)
  println(fr4r)

  /**
    * different contracts:
    * foldLeft:  f:(acc, a) => acc
    * foldRight: f:(a, acc) => acc
    */
}
