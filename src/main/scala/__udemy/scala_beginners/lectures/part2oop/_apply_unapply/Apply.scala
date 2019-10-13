package __udemy.scala_beginners.lectures.part2oop._apply_unapply

object Apply extends App {
  class Dog(val name: String) {
    override def toString = s"Dog{name:$name}"
  }

  object Dog {  // singleton === static
    val Jack = "Jack"
    val Mirror = "Mirror"
  }

  class SmartDog(val d1: Dog, val d2:Dog) {
    override def toString = s"SmartDog($d1, $d2)"
  }

  object SmartDog { // singleton === static
    def apply(d1: Dog, d2: Dog): SmartDog = new SmartDog(d1, d2)
  }

  val d1 = new Dog(Dog.Jack)
  val d2 = new Dog(Dog.Mirror)
  val sd = new SmartDog(d1, d2)

  val sd21 = SmartDog.apply(d1, d2)
  val sd22 = SmartDog(d1, d2) // apply

  println(sd)
  println(sd21)
  println(sd22)
}
