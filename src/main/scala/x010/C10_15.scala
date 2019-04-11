package x010

object C10_15 extends App {
  val li = List(List(1,2), List(3,4))
  val li2 = List(List(1,2), List(3,4, List(5,6)))
  println(li)
  println(li.flatten) // 1 level
  println(li2.flatten)
  // flatten skips all None
  println(Vector(Some(1), None, Some(2)).flatten)
}
