package lessons.typeclasses

object x03_TypeClassResolution extends App {

  trait Named[E] {
    val name: String
    def print(): Unit = println(name)
  }

  implicit val int: Named[Int] = new Named[Int] {
    override val name: String = "int"
  }

  implicit val chr: Named[Char] = new Named[Char] {
    override val name: String = "char"
  }

  implicit val str: Named[String] = new Named[String] {
    override val name: String = "string"
  }

  // this instance resolved implicitly in compile time!
  val instance: Named[Int] = implicitly[Named[Int]]
  val name: String = instance.name
  instance.print()

}
