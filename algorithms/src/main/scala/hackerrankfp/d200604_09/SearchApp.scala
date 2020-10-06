package hackerrankfp.d200604_09

import scala.collection.mutable.ArrayBuffer

object SearchApp extends App {
  case class A(a: Int)
  val v = Vector(A(2), A(1), A(2), A(1))
  val r = v.search(A(1))((x,y) => x.a - y.a)
  println(r)
}

object InsertApp extends App {
  val ab = ArrayBuffer(10,20,30,40)
//  ab.insert(0,5)
  ab.insert(4, 45)
  println(ab)
}
