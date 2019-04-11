package x010

object C10_19 extends App {
  val l = List.range(1,15)
  println(l)
  println(l.groupBy(_ % 3)) // Map(2 -> List(2, 5, 8, 11, 14), 1 -> List(1, 4, 7, 10, 13), 0 -> List(3, 6, 9, 12))
  println(l.partition(_ % 2 == 0)) // (List(2, 4, 6, 8, 10, 12, 14),List(1, 3, 5, 7, 9, 11, 13))
  println(l.span(_ < 12)) // (List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),List(12, 13, 14))
  println(l.splitAt(3)) // (List(1, 2, 3),List(4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14))
  println(l.sliding(3).toList) // List(List(1, 2, 3), List(2, 3, 4), List(3, 4, 5), List(4, 5, 6), ...
}
