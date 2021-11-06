package udemy.scala_beginners.lectures.part2oop._constructors

object PersonTest extends App {
  val p1 = new SmartPerson("Alex", "Rykhalskiy", 43)
  val p2 = new SmartPerson("Alex", "Rykhalskiy")
  val p3 = new SmartPerson("Alex")

  println(p1)
  println(p2)
  println(p3)
  println(p1.toString1)
  println(p2.toString1)
  println(p3.toString1)
  val a = p1.age
  val b = p1.name
  val c = p1.surName
}
