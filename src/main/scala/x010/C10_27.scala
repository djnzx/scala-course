package x010

object C10_27 extends App {
  val t1 = ("Debt", 95)
  println(t1)
  val el1 = t1._1
  val el2 = t1._2
  val (n, v) = t1
  val (n1, _) = t1
  println(n)
  println(v)
  val t2 = "Alex" -> (43 -> 44)
  val t3 = "Alex" -> 43 -> 44 // ((Alex,43),44)
  println(t2)
  println(t3)
  t3.productIterator.foreach(println)
  t3.productIterator.toArray
}
