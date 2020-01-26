package aa_cookbook.x006

object ApplyApp extends App {

//  case class Person (val name: String, val age: Int)

  class Person (val name: String, val age: Int) {
    def this(name: String) {
      this(name, -1)
    }
    def this(list: List[String]) {
      this(list.head, -1)
    }
    override def toString: String = s"Person:[name:$name, age:$age]"
  }

  object Person {
    def apply(name: String, age: Int) = new Person(name, age)
    def apply(name: String) = new Person(name)
  }

  val p1 = new Person("Alex", 43)
  val p11 = new Person("Alex")
  val p2 = Person("Alex", 43)
  val p21 = Person("Alex")
  println(p1)
  println(p11)
  println(p2)
  println(p21)
  println(p2 == p1)
}
