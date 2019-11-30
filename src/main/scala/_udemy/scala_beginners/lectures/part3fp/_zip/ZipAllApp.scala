package _udemy.scala_beginners.lectures.part3fp._zip

object ZipAllApp extends App {
  val list1 = List("a", "b", "c") // List[String] = List(a, b, c)
  val list2 = List("x", "y") // List[String] = List(x, y)
  val r1 = list1 zipAll(list2, "AAA", "BBB")
  val r2 = r1.map(t=>s"${t._1}${t._2}")
  println(r1)
  println(r2)
}
