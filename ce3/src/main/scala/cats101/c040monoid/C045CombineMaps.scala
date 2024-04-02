package cats101.c040monoid

import cats.Monoid
import cats.implicits.catsSyntaxSemigroup

object C045CombineMaps extends App {

  val map1 = Map("a" -> 1, "b" -> 2)
  val map2 = Map("b" -> 3, "d" -> 4)

  val map3a = Monoid[Map[String, Int]].combine(map1, map2)
  val map3b = map1 |+| map2
  // Map(b -> 5, d -> 4, a -> 1)

  println(map3a)
  println(map3b)
}
