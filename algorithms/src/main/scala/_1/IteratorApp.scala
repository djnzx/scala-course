package _1

object IteratorApp extends App {
  val x = List(1,2,3)
  val it0 = x.iterator // doesn't work, because iterator is single used
  def it = x.iterator
  it.foreach(println)
  it.foreach(println)
  println(it0.size)
  it0.foreach(println)
  val (r1, r2) = (1 to 10).iterator.map(_+10).splitAt(3)
  r1.foreach(println)
  r2.foreach(println)
  println("--------------")
  println("--------------")
}
