package catsx.c001show

object C021Show extends App {

  import cats._
  import cats.implicits._

  // instances
  val showInt: Show[Int] = Show.apply[Int] // extract implicit from scope (as in line 21)
  val showString: Show[String] = Show.apply[String]
  implicit val showCat: Show[Cat] = C020Show.MyImplicitInstances.showCat

  val cat: Cat = Cat("Barcelona", 7)

  // explicit call on extracted implicits
  val s1: String = showInt.show(10)
  val s2: String = showString.show("Hello")
  val s3: String = showCat.show(cat)
  // find implicit manually
  val s4: String = implicitly[Show[Int]].show(10)

  // syntax because of implicit conversion from `cats.implicits._`
  val s5: String = 123.show
  val s6: String = "Hello".show
  val s7: String = 3.5.show
  // syntax because of line 7 or 12
  val s8: String = cat.show
}
