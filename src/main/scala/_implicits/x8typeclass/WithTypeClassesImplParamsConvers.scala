package _implicits.x8typeclass

object WithTypeClassesImplParamsConvers extends App {
  final case class Person(firstName: String, lastName: String)
  final case class Dog(name: String)

  val person = Person("Alex", "Petroff")
  val dog = Dog("Jack")

  // implicit declaration which will be attached to any type A
  implicit class ChatUtil[A](x: A) {
    def chat(implicit makesChatty: CanChat[A]): String = makesChatty.chat_(x)
  }
  // implicit implementations, not tied
  implicit object PersonCanChat extends CanChat[Person] {
    def chat_(x: Person) = s"Hi, I'm ${x.firstName}"
  }
  implicit object DogCanChat extends CanChat[Dog] {
    def chat_(x: Dog) = s"Woof, my name is ${x.name}"
  }
  implicit object StringCanChat extends CanChat[String] {
    def chat_(a: String): String = s"I'm a string with a val:$a"
  }
  implicit object IntCanChat extends CanChat[Int] {
    def chat_(a: Int): String = s"I'm an int with a val:$a"
  }

  // here we call ChatUtil.chat
  val s1 = person.chat
  val s2 = dog.chat
  val s3 = "STRING".chat
  val s4 = 1.chat
  println(s"$s1\n$s2\n$s3\n$s4\n")


}
