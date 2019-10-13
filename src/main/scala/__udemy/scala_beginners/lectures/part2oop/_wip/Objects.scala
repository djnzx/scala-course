package __udemy.scala_beginners.lectures.part2oop._wip

/**
  * Created by Daniel.
  */
object Objects extends App {

  // SCALA DOES NOT HAVE CLASS-LEVEL FUNCTIONALITY ("static")
  object Person { // type + its only instance
    // "static"/"class" - level functionality
    val N_EYES = 2
    def canFly: Boolean = false

    // factory method
    def apply(mother: Person, father: Person): Person = new Person("Bobbie")
  }

  class Person(val name: String) {
    // instance-level functionality
    //override def equals(obj: Any): Boolean = ((Person)super).name.equals(((Person)obj).name)
  }
  // COMPANIONS

  //println(Person.N_EYES)
  //println(Person.canFly)

  // Scala object = SINGLETON INSTANCE
  val mary = new Person("Mary")
  val john = new Person("John")
  println(mary == john)

  val person1 = Person
  val person2 = Person
  println("object comparing:")
  println(person1 == person2)

  val person1_ = new Person("Alex")
  val person2_ = new Person("Alex")

  printf("person1_: %s\n", person1_)
  printf("person2_: %s\n", person2_)

  println("==:")
  println(person1_ == person2_)
  println("equals:")
  println(person1_ equals person2_)
  println("eq:")
  println(person1_ eq person2_)


  val bobbie = Person(mary, john)
  val bobbie2 = Person.apply(mary, john)
  // Scala Applications = Scala object with
  // def main(args: Array[String]): Unit


  val k = 6.67e-11

}
