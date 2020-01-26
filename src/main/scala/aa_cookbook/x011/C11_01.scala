package aa_cookbook.x011

import scala.collection.mutable.ListBuffer

object C11_01 extends App {
  val l1 = 1 :: 2 :: 3 :: Nil
  val l2 = List(1,2,3)
  val l3 = List[Int](1,2,3)
  val l4 = List.range(1,10)
  val l5 = List.range(1,10,2)
  // mutable
  val l6 = collection.mutable.ListBuffer(1,2,3).toList

  println(List.fill(5)(10))
  println(List.tabulate(10)(x => x * x))

  val fruits = new ListBuffer[String]
  fruits += "Apple"
  fruits += "Banana"
  fruits += ("Orange", "Plum")
  fruits -= ("Orange", "Banana")
  fruits -= "Apple"
  l1 :+ 7
  0 +: l2
  l1 ::: l2
}
