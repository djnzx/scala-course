package cats

import cats._ // instead of import cats.Show
import cats.implicits._
//import cats.instances.int._ // for Show
//import cats.instances.string._
//import cats.syntax._

object C020 extends App {
  case class Cat(name: String, age: Int)
  val bars: Cat = Cat("Barcelona", 8)

  object MyImplicitInstances {
    // approach 1
    implicit val cat_show: Show[Cat] = c => s"cat: ${c.name}, ${c.age} y.o."
    // approach 2
//    implicit val cat_show: Show[Cat] = Show.show(c => s"cat: ${c.name}, ${c.age} y.o.")
  }

  import MyImplicitInstances._

  val s1: String = 123.show
  val s2: String = 3.5.show
  val s3: String = bars.show
  println(s3)

}
