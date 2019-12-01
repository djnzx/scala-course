package x71cats

import cats.Monoid
import cats.implicits._
// or import cats.instances.string._

object C040Monoids extends App {

  // different approaches of extracting instances
  val inst1: Monoid[String] = implicitly[Monoid[String]]
  val inst2: Monoid[String] = Monoid.apply[String]
  val inst3: Monoid[String] = Monoid[String]

  val empty: String = Monoid[String].empty

  // approach 1
  val combined1: String = inst1.combine("Hi ", "there")

  // approach 2
  val combined2: String = Monoid[String].combine("Hi ", "there")

  val a = Option(22)
  val b = Option(20)
  val c1: Option[Int] = Monoid[Option[Int]].combine(a, b)
  val c2 = a |+| b
  println(c2)

  def add1[A]        (items: List[A])(implicit ev: Monoid[A]): A = items.foldLeft(ev.empty)((a, b) => a |+| b)
  def add2[A: Monoid](items: List[A])                        : A = items.foldLeft(Monoid[A].empty)((a, b) => a |+| b)
  def add3[A: Monoid](items: List[A])                        : A = items.foldLeft(Monoid[A].empty)(_ |+| _)
  println(add1(List(1,2,3,4)))
  println(add2(List(1,2,3,4)))

  case class Order(totalCost: Double, quantity: Double)
  // simply define a monoid instance for Order!
  implicit val order_monoid: Monoid[Order] = new Monoid[Order] {
    override def empty: Order = Order(0, 0)
    override def combine(x: Order, y: Order): Order = Order(x.totalCost |+| y.totalCost, x.quantity |+| y.quantity)
  }
  println(add2(List(
    Order(1,10),
    Order(2,20),
    Order(3,30),
    Order(4,40),
  )))

  val map1 = Map("a" -> 1, "b" -> 2)
  val map2 = Map("b" -> 3, "d" -> 4)
  val map3 = map1 |+| map2 // Map(b -> 5, d -> 4, a -> 1)
  println(map3)
  val tuple1 = ("Hello ", 123)
  val tuple2 = ("World", 321)
  val tuple3 = tuple1 |+| tuple2
  println(tuple3)

}
