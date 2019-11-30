package _udemy.scala_beginners.lectures.part3fp._map

object MapSample2 extends App {
  val users: Map[String, Set[Int]] = Map("Alex"-> Set(1,2), "Dima"->Set(2,3), "Serg"->Set())
  val users2 = users + ("Alex" -> (users("Alex") + 11)) + ("Dima" -> (users("Dima") - 3 - 33)) - "Serg"
  println(users)
  println(users2)

  val m1 = Map("Sasha"->1, "Dima"->2)
  val m2 = Map("Masha"->3, "Lena"->4)
  val m3 = m1 ++ m2
  println(m1)
  println(m2)
  println(m3)

  val m4 = m3 -- List("Sasha", "Dima")
  println(m4)
  val r = m2 == m4
  println(s"m2 == m4: $r")
}
