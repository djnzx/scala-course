package cats101.c040monoid

import cats.Monoid
import cats.implicits.catsSyntaxSemigroup

object C040Monoids extends App {

  /**
    * Monoid has:
    * - empty element
    * - operation
    */
  // different approaches to extract instance from scope
  val inst1: Monoid[String] = implicitly[Monoid[String]]
  val inst2: Monoid[String] = Monoid.apply[String]
  val inst3: Monoid[String] = Monoid[String]

  // that's empty element for string concatenation
  val empty: String = Monoid[String].empty

  // approach 1
  val combined1: String = inst1.combine("Hi ", "there")

  // approach 2
  val combined2: String = Monoid[String].combine("Hi ", "there")

  val a = Option(22)
  val b = Option(20)
  val c1: Option[Int] = Monoid[Option[Int]].combine(a, b)
  val c2 = a |+| b // Some(42)

  def add1[A](items: List[A])(implicit ev: Monoid[A]): A = items.foldLeft(ev.empty)((a, b) => ev.combine(a, b))
  def add2[A](items: List[A])(implicit ev: Monoid[A]): A = items.foldLeft(ev.empty)((a, b) => a |+| b)
  def add3[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)((a, b) => a |+| b)
  def add4[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)

  println(add2(List(1, 2, 3, 4)))

}
