package x010

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object C10_02 extends App {
  val a1 = Vector(1,2,3) // immutable array
  val a2 = ArrayBuffer(1,2,3) // mutable array

  val l1 = List("a", "b", "c") // immutable linked list
  val l2 = ListBuffer("a", "b", "c") // mutable linked list ArrayBuffer

  val a11 = a1 :+ 4
  val a12 = 0 +: a11
  println(a1)
  println(a11)
  println(a12)

  a2.prepend(0)
  a2.append(4)
  println(a2)

  val m = Map(1->"a", 2->"b", 3->true)
  val m2 = m + (4->5.5)
  println(m)
  println(m2)
  println(m2(2))
  println(m2.getOrElse(5, "NO EL"))

}
