package x00topics.implicits.i4tinstance_by_type.Application06

object WithTypeClassesImplParams extends App {
  final case class Person(firstName: String, lastName: String)
  final case class Dog(name: String)

  val person = Person("Alex", "Petroff")
  val dog = Dog("Jack")

  object ChatUtil {
    def chat[A](x: A)(implicit chattyThing: CanChat[A]) = {
      chattyThing.chat_(x)
    }
  }

  implicit object PersonCanChat extends CanChat[Person] {
    def chat_(x: Person) = s"Hi, I'm ${x.firstName}"
  }
  implicit object DogCanChat extends CanChat[Dog] {
    def chat_(x: Dog) = s"Woof, my name is ${x.name}"
  }

  val s1 = ChatUtil.chat(dog)
  val s2 = ChatUtil.chat(person)
  println(s"$s1\n$s2\n")
}
