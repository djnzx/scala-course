package _implicits.x8typeclass

object WithTypeClasses extends App {
  final case class Person(firstName: String, lastName: String)
  final case class Dog(name: String)

  val person = Person("Alex", "Petroff")
  val dog = Dog("Jack")

  object PersonCanChat extends CanChat[Person] {
    def chat_(x: Person) = s"Hi, I'm ${x.firstName}"
  }

  object PersonCanChatFormally extends CanChat[Person] {
    def chat_(x: Person) = s"Hello, I'm ${x.firstName} ${x.lastName}"
  }

  object DogCanChat extends CanChat[Dog] {
    def chat_(x: Dog) = s"Woof, my name is ${x.name}"
  }

  object ChatUtil {
    def chat[A](x: A, chattyThing: CanChat[A]) = {
      chattyThing.chat_(x)
    }
  }

  val s1 = ChatUtil.chat(dog, DogCanChat)
  val s2 = ChatUtil.chat(person, PersonCanChat)
  val s3 = ChatUtil.chat(person, PersonCanChatFormally)
  println(s"$s1\n$s2\n$s3\n")
}
