package __udemy.scala_beginners.lectures.part3fp._list._immutable_content

object ListTest extends App {
  val p1 = Persn("Alex", 20)
  val p2 = Persn("Alex", 20)
  val list = List(p1, p2)
  println(list)
  println(list.distinct) // work because case class
}
