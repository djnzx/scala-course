package x004

object Equality extends App {
  //  case class Person(name: String, age: Int)
  class Person(val name: String, val age: Int) {

    def canEqual(that: Any) = that.isInstanceOf[Person]

    override def equals(that: Any): Boolean =
      that match {
        case Person(name, age) => this.canEqual(that) && this.## == that.##
        case _ => false
      }

    override def hashCode(): Int = {
      val prime = 31
      var res = 1
      res = prime * res + age
      res = prime * res + (if (name == null) 0 else name.hashCode)
      res
    }

  }

  object Person {
    def apply(name: String, age: Int) = new Person(name, age)
    def unapply(person: Person): Option[(String, Int)] = { Some((person.name, person.age))}
  }

  val p1 = Person("Alex", 42) // thankfully Person.apply
  val p2 = Person("Alex", 42)
  val p3 = Person("Dima", 44)

  println(p1.equals(p2))
  println(p1 equals p2)
  println(p1.==(p2))
  println(p1 == p2)

  p3 match {
    case Person(name, age) => println(s"name: $name, age: $age") // thankfully Person.unapply
    case _ => println("smth else")
  }


}
