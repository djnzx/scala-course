package catsx.c040monoid

import cats.Monoid
import cats.implicits.catsSyntaxSemigroup

object C043CombineOrder extends App {
  def add(items: List[Int]): Int = items.foldLeft(Monoid[Int].empty)(_ |+| _)

  def add2[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)

  import cats.implicits._ // Monoid[Option[Int]]

  println(implicitly[Monoid[Option[Int]]])
  println(add(List(1, 2, 3)))
  println(add2[Option[Int]](List(Some(1), Some(2), Some(3))))

  case class Order(totalCost: Double, quantity: Double)

  implicit val monoidOrder: Monoid[Order] = new Monoid[Order] {
    override def empty: Order = Order(0, 0)

    override def combine(x: Order, y: Order): Order = Order(
      x.totalCost |+| y.totalCost,
      x.quantity |+| y.quantity
    )
  }

  println(add2(List(
    Order(1, 10),
    Order(2, 20),
    Order(3, 30),
    Order(4, 40),
  )))
}
