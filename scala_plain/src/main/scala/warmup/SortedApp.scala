package warmup

object SortedApp extends App {
  val m = Map("apple" -> 0.69, "orange" -> 0.79, "watermelon" -> 1.10, "peach" -> 0.89)
  val r = m.toVector.sortWith { (e1, e2) => e1._2 > e2._2 } map { _._1 }
  println(r)
}
