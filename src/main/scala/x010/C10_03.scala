package x010

import scala.collection.mutable.ArrayBuffer

object C10_03 extends App {
  val grouped = List.range(1, 10).groupBy(_ < 5)
  println(grouped)
  List.range(1,10).reduceLeft((a, b) => a + 2 * b)

  val a1 = Vector.range(1,10)
  val a2 = List.range(1,10)

  println(a1)
  println(a2)

  val a3 = a1.updated(7, 88)
  println(a3)

  val ab = new ArrayBuffer[Int]()
  ab.append(1)
  ab.append(2)
  ab.append(3)
  println(ab)
  ab(0)=11
  println(ab)
  ab -= 11
  println(ab)
  ab += 22
  println(ab)


}
