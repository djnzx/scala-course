package _implicits.x8typeclass

object WithTypeClassesImplParamsConvers extends App {
  final case class Person(firstName: String, lastName: String)
  final case class Dog(name: String)

  val person = Person("Alex", "Petroff")
  val dog = Dog("Jack")

  // implicit declaration which will be attached to any type A
  implicit class ChatMixin[A](x: A) {
    def chat(implicit canChat: CanChat[A]): String = canChat.chat_(x)
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

  /** declaring implementation
    * way 1 - Object (Singleton, class with one static method)
    */
  implicit object IntCanChat extends CanChat[Int] {
    def chat_(a: Int): String = s"I'm an int (local implementation has higher priority than imported) with a val:$a"
  }
  /** declaring implementation
    * way 2 - making instance of Interface with according type
    */
//  implicit val intCanChat = new CanChat[Int] {
//    override def chat_(a: Int): String = s"I'm an int (implicit val) with a val:$a"
//  }

  // here we call .chat() from ChatUtil.chat
  val s1 = person.chat
  val s2 = dog.chat
  val s3 = "STRING".chat
  val s41 = 1.chat
  /** declaring implementation
    * way 3 - inlined implementation of Interface with according type
    */
  val s42 = 1.chat( new CanChat[Int]() {
    override def chat_(a: Int): String = s"I'm an int (inline implementation #1) with a val:$a"
  })
  /** declaring implementation
    * way 4 - inlined implementation of Interface with according type
    * simplified syntax if only one method in the interface
    */
  val s43 = 1.chat((a: Int) => s"I'm an int (inline implementation #2) with a val:$a")
  println(s"$s1\n$s2\n$s3\n$s41\n$s42\n$s43\n")
}
