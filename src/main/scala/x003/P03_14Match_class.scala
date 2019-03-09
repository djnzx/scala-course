package x003

object P03_14Match_class extends App {
  trait Sentient

  case class Dog(name: String) extends Sentient
  case class Person(first: String, last: String) extends Sentient

  class Cat(val name: String) extends Sentient
  object Cat {
    def apply(name: String) = new Cat(name) // need to write `x = Cat(name)` instead of `x = new Cat(name)`
    def unapply(cat: Cat): Option[String] = Some(cat.name) // should return Option(*) to use
  }

  def decision(c: Sentient) = c match {
    case Dog(name) => println(s"Dog: $name")
    // by variable type, in that case we don't need unapply
//    case cat: Cat => println(s"Cat: ${cat.name}")
    // by class with help unapply function
    case Cat(name) => println(s"Cat: $name")
    case Person(first, last) => println(s"Person: $first $last")
    case _ => println("unknown Sentient")
  }

  decision(new Dog("Jack"))
  decision(Dog("Mortal")) // companion object already by design case class
  decision(Person("Alexr", "R"))
  decision(new Cat("Masyanya"))
  decision(Cat("Barcelona"))  // to use that we should write companion object with apply function
  val d = Dog("Micro")
//  d.name = "Jack" final by design

}
