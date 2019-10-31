package x00lessons.typeclasses

object x04_TypeclassInduction extends App {

  type EOL = Unit

  trait Named[E] { val name: String }

  // base step
  implicit val base: Named[EOL] = new Named[EOL] {
    override val name: String = "x"
  }

  // induction step
  implicit def step[H, T](implicit head: Named[H], tail: Named[T]) = new Named[(H, T)] {
    override val name: String = s"${head.name}-${tail.name}"
  }

  implicit val namedInt: Named[Int] = new Named[Int] {
    override val name: String = "int"
  }

  implicit val namedChr: Named[Char] = new Named[Char] {
    override val name: String = "char"
  }

  implicit val namedStr: Named[String] = new Named[String] {
    override val name: String = "string"
  }

  // manual implicit resolution compile time for type classes
  val instance1 = implicitly[Named[Int]]
  println(instance1.name)
  // recursive resolution
  val instance2 = implicitly[Named[(Int, (Int, (Int, (Char, (String, EOL)))))]]
  println(instance2.name)

}
