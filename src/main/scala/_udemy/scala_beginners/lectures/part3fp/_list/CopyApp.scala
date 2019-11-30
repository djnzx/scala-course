package _udemy.scala_beginners.lectures.part3fp._list

object CopyApp extends App {
  val p1 = PersonMutable("Alex", 30)
  // copy only on case classes
  val p2 = p1.copy(age = 20)
  val list = List(p1, p2)
  println(list)
}
