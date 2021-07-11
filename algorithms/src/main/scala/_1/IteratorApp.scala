package _1

object IteratorApp extends App {
  val x = List(1,2,3)
  val it0 = x.iterator // doesn't work, because iterator is single used
  def it = x.iterator
  it.foreach(println)
  it.foreach(println)
}
