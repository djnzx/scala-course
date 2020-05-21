package catseffx.gabriel

import scala.collection.SortedSet

object SortedSetApp extends App {
  val set = Set(1,2,3,4,5,6,7,8,9,0)
  println(set)
  println(SortedSet(set.toSeq: _*))
}
