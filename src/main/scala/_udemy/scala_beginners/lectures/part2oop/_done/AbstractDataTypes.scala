package _udemy.scala_beginners.lectures.part2oop._done

/**
  * Created by Daniel.
  */
object AbstractDataTypes extends App {

  // abstract
  abstract class Animal {
    val creatureType: String = "wild"
    def eat: Unit
  }

  class Dog extends Animal {
    override val creatureType: String = "Canine"
    def eat: Unit = println("crunch crunch")
    def default: Animal = new Dog
  }

  // traits
  trait Carnivore {
    def eat(animal: Animal): Unit
    val preferredMeal: String = "fresh meat"
  }

  trait C2 extends Carnivore {
    def eat2: Unit
  }

  trait ColdBlooded

  class Crocodile extends Animal with Carnivore with ColdBlooded {
    override val creatureType: String = "croc"
    def eat: Unit = println("nomnomnom")
    def eat(animal: Animal): Unit =
      println(s"I'm a croc and I'm eating ${animal.creatureType}")
  }

  class Crocodile2 extends C2 {
    override def eat2: Unit = println("method from c2")

    override def eat(animal: Animal): Unit = println("method from carnivore")
  }

  val dog = new Dog
  val croc = new Crocodile
  croc.eat // nomnomnom
  croc.eat(dog)

  val c2 = new Crocodile2

  printf("The crocodile is instance of ColdBlooded=%s\n",croc.isInstanceOf[ColdBlooded])


/*
  val a = new Animal {
    override def eat: Unit = println("I'm animal")
  }

  println(a)
  a.eat

  val d = new Dog
  println(d)
  d.eat
*/

  class Alex {
    def think: Unit = println("I'm thinking..")
    def read(book : String) = println(s"I'm reading $book")
  }

  object Alex {
    val Java = "Java"
    val Scala = "Scala"
  }

  val al = new Alex
  al.think
  al.read(Alex.Java)


  // traits vs abstract classes
  // 1 - traits do not have constructor parameters
  // 2 - multiple traits may be  inherited by the same class
  // 3 - traits = behavior, abstract class = "thing"
}
