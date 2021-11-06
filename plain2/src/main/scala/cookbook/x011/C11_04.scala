package cookbook.x011

import scala.collection.mutable.ListBuffer

object C11_04 extends App {
  val l1 = ListBuffer.range(1,20)
  val l2 = ListBuffer.range(1,10)
  // only for mutable
  l1 --= l2
  // by value
  l1 -= (5,6)
  l1 -= 5
  // by index
  l1.remove(5)

  // for mutable
  l1 ++ l2

  val l21 = List.range(1,20)
  val l22 = List.range(1,10)
  // for immutable list only
  val l7 = l21 ::: l22



}
