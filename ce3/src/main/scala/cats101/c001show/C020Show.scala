package cats101.c001show

import cats.Show
import cats.implicits.toShow

object C020Show extends App {

  val bars: Cat = Cat("Barcelona", 8)

  object MyImplicitInstances {
    // approach 1
    implicit val showCat: Show[Cat] = c => s"cat: ${c.name}, ${c.age} y.o."
    // approach 2
    //    implicit val cat_show: Show[Cat] = Show.show(c => s"cat: ${c.name}, ${c.age} y.o.")
  }

  import MyImplicitInstances.showCat

  val s1: String = 123.show
  val s2: String = 3.5.show
  val s3: String = bars.show

}
