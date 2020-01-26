package udemy.scala_beginners.lectures.part3fp._list

// try to avoid var !!!!!!!
case class PersonMutable(name: String, var age: Int)

object PersonMutable {
  def apply(name: String, age: Int): PersonMutable = new PersonMutable(name, age)
}

object ListTest extends App {
  val p1 = PersonMutable.apply("Alex", 30)
  val p2 = PersonMutable("Demon", 31)
  val p3 = new PersonMutable("Demon", 31)
  val list = List(p1, p2)
  println(list)

  p2.age = 11
  println(list(0))
  println(list head)

  val pp1 = list(1)
  println(pp1)
  println(list)

  pp1.age = 12
  println(list)
}
