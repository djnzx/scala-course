package map_problems

import pprint.pprintln

object MapMergeApp extends App {

  val m1 = Map(1 -> "a", 2 -> "b")
  val m2 = Map(2 -> "c", 3 -> "d")
  pprintln(m1 ++ m2)
  pprintln(m2 ++ m1)

}
