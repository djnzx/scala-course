package rtj.essentials

object Recap {

  val a: Boolean = true

  val ifEx: String = if (2 > 3) "bigger" else "smaller"

  val unit: Unit = println("ABC") // (), kind of empty tuple

  class Animal
  class Cat extends Animal

  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(animal: Animal): Unit = println("Done!")
  }
  // singleton
  object Me

  // static stuff are here
  object Carnivore

  val three: Int = 1.+(2)

}
