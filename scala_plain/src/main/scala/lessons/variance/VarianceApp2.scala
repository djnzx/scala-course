package lessons.variance

object VarianceApp2 extends App {
  /**
    * A Function1 is contravariant over its argument type,
    * and covariant over its return type.
    * Function1[A, B] === A => B
    */
  abstract class Animal {
    def name: String
  }
  case class Cat(name: String) extends Animal
  case class Dog(name: String) extends Animal
  abstract class SmallAnimal extends Animal
  case class Mouse(name: String) extends SmallAnimal

  /**
    * Suppose we’re working with functions that accept types of animals,
    * and return the types of food they eat.
    *
    * If we would like a Cat => SmallAnimal (because cats eat small animals),
    * but are given a Animal => Mouse instead, our program will still work.
    *
    * Intuitively an Animal => Mouse will still accept a Cat as an argument,
    * because a Cat is an Animal, and it returns a Mouse, which is also a SmallAnimal.
    *
    * Since we can safely and invisibly substitute the former by the latter,
    * we can say Animal => Mouse is a subtype of Cat => SmallAnimal.
    */

  val eat: Function1[Animal, SmallAnimal] = animal => { println(s"animal ${animal.name}"); Mouse("mice") }
  eat(Cat("Alice"))
  eat(Dog("Jack"))
  eat(Mouse("mi-mi.."))

}
