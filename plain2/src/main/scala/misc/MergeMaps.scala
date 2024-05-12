package misc

import cats.implicits.catsSyntaxSemigroup
import pprint.pprintln

object MergeMaps extends App {

  val m1 = Map(1 -> "a", 2 -> "b")
  val m2 = Map(2 -> "c", 3 -> "d")
  pprintln(m1 ++ m2)
  pprintln(m2 ++ m1)
  pprintln(m2 |+| m1)

}
