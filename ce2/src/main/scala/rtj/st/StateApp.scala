package rtj.st

import cats._
import cats.data._
import cats.implicits._

object StateApp extends App {

  case class ShoppingCart(items: List[String], total: Double)
  def addToCart(item: String, amount: Double): State[ShoppingCart, Double] = State[ShoppingCart, Double] {
    case ShoppingCart(items, total) =>
      val newTotal = total + amount
      (ShoppingCart(item :: items, newTotal), newTotal)
  }

  def inspect[A, B](f: A => B): State[A, B] = State[A, B](a => (a, f(a)))

}
