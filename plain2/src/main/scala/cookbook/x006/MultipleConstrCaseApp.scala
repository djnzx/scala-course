package cookbook.x006

object MultipleConstrCaseApp extends App {
  case class Person1 (name: String, age: Int) {
    def this(list: List[String]) {
      this(list.head, -1)
    }
    def this() {
      this("<not given>", -1)
    }
  }
  object Person1 {
    def apply(list: List[String]) = new Person1(list)
    def apply() = new Person1()
  }

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

  val p5 = new Person1("Alex", 43)
  val p6 = new Person1(List("qwe", "asd"))
  val p7 = Person1("Alex", 43)
  val p8 = Person1(List("qwe", "asd"))
  val p9 = Person1()
  println(p5)
  println(p6)
  println(p7)
  println(p8)
  println(p9)

}
