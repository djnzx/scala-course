package catsx

import cats.Semigroupal
import cats.instances.option._
import cats.syntax.apply._

object C145Semi extends App {
  val a: Option[Int] = Some(123)
  val b: Option[String] = Some("abc")

  val c1: Option[(Int, String)] = Semigroupal[Option].product(a, b)

  val c2: Option[(Int, Int, Int)] = Semigroupal.tuple3(Option(1), Option(2), Option(3))

  val c3: Option[Int] = Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)

  val t1: Option[(Int, String)] = (Option(123), Option("abc")).tupled

  case class Cat(name: String, born: Int, color: String)

  val cat1: Option[Cat] = Semigroupal.map3(
    Option("Garfield"),
    Option(1978),
    Option("Orange & black")
  )(Cat.apply)

  val cat2: Option[Cat] = (
    Option("Garfield"),
    Option(1978),
    Option("Orange & black")
    ).mapN(Cat.apply) // pass all elements from the tuple to the given function
}
