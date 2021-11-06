package cookbook.x003

object Lists extends App {
  val l3 = List(6,7,8,9)
  val lh = l3.head
  val lt = l3.tail
  val i4 = l3.takeRight(2) // 8 9
  val i5 = l3.take(2) // 6 7
  val i6 = l3.takeWhile(_<9) // 6 7 8
  println(i4.mkString(" "))
  println(i5.mkString(" "))
  println(i6.mkString(" "))
}
