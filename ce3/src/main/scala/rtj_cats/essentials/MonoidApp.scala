package rtj_cats.essentials

import cats._
import cats.implicits._

object MonoidApp extends App {

  case class ShoppingCart(items: List[String], total: Double)

  implicit val shoppingCartMonoid = Monoid.instance[ShoppingCart](
    ShoppingCart(Nil, 0),
    { case (ShoppingCart(i1, t1), ShoppingCart(i2, t2)) =>
      ShoppingCart(i1 ++ i2, t1 + t2)
    }
  )

  def checkout(xs: List[ShoppingCart])(implicit m: Monoid[ShoppingCart]): ShoppingCart = xs.foldLeft(m.empty)(m.combine)

}
