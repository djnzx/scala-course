package _implicits.x8typeclass

object WithoutTypeClasses extends App {
  final case class Person(firstName: String, lastName: String)
  final case class Dog(name: String)

  val person = Person("Alex", "Petroff")
  val dog = Dog("Jack")

  // way1
  object PersonCanChat {
    def chat(x: Person) = s"W1:Hi, I'm ${x.firstName}"
  }
  object DogCanChat {
    def chat(x: Dog) = s"W1:Hi, I'm ${x.name}"
  }
  println(PersonCanChat.chat(person))
  println(DogCanChat.chat(dog))

  // way 2
  implicit class PersonCanChat(p: Person) {
    def chat() = s"W2:Hi, I'm ${p.firstName}"
  }
  implicit class DogCanChat(d: Dog) {
    def chat() = s"W2:Hi, I'm ${d.name}"
  }
  println(person.chat())
  println(dog.chat())
}
