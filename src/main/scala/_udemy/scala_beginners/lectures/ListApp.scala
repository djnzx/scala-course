package _udemy.scala_beginners.lectures

object ListApp extends App {
  val l = List(1,2,3)
  println(l)

  val l1 = l :+ 4
  print(l1)

  val l2 = 0 +: l1
  println(l2)
}
