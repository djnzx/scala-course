package cats101.c040monoid

import cats.implicits.catsSyntaxSemigroup

object C044CombineTuples extends App {
  val tuple1 = ("Hello ", 123)
  val tuple2 = ("World", 321)

  val tuple3 = tuple1 |+| tuple2

  println(tuple3)
}
