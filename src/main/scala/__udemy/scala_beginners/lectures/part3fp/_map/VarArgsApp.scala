package __udemy.scala_beginners.lectures.part3fp._map

object VarArgsApp extends App {
  def test01(items: String*): List[String] =
    items.toList

  var users1 = test01("Alex")
  println(users1)
  var users2 = test01("Alex", "Dima")
  println(users2)
  var users3 = test01("Alex", "Dima", "Serg")
  println(users3)

}
