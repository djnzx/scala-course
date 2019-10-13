package __udemy.scala_beginners.lectures.part3fp._map_flatmap_filter_for

object MapFlatmapFilterFor2 extends App {
  val list = List(1,2,3)
  println(list.head)
  println(list.tail)

  // map
  println(list.map(_ + 1))
  println(list.map(_ + " is a number"))

  // filter
  println(list.filter(_ % 2 == 0))

  // flatMap
  val toPair = (x: Int) => List(x, x+1)
  def toPairF(x: Int) = List(x, x+1)
  println(list.map(toPair))  // List(List(1, 2), List(2, 3), List(3, 4))
  println(list.map(toPairF))  // List(List(1, 2), List(2, 3), List(3, 4))
  println(list.flatMap(toPair)) // List(1, 2, 2, 3, 3, 4)
  println("====")
}
