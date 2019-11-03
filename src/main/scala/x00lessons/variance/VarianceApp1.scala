package x00lessons.variance

object VarianceApp1 extends App {

  /**
    * https://docs.scala-lang.org/tour/variances.html
    *
    * class Foo[+A] // A covariant class     <? extends T>
    * class Bar[-A] // A contravariant class <? super T>
    * class Baz[A]  // An invariant class    <T>
    *
    */
  abstract class Animal {
    def name: String
  }
  /**
    * Both `Cat` and `Dog` are subtypes of `Animal`
    *
    * List[+A]:
    * List[Animal] means:
    *   List[Cat] is a List[Animal]
    *   List[Dog] is also a List[Animal]
    */
  case class Cat(name: String) extends Animal
  case class Dog(name: String) extends Animal

  /**
    * Covariance test, Up the hierarchy
    */
  def printAnimalNames(animals: List[Animal]): Unit = {
    animals.foreach { animal =>
      println(animal.name)
    }
  }
  val cats: List[Cat] = List(Cat("Whiskers"), Cat("Tom"))
  val dogs: List[Dog] = List(Dog("Fido"), Dog("Rex"))
//  printAnimalNames(cats)
//  printAnimalNames(dogs)

  /**
    * contravariant
    *
    * If a Printer[Cat] knows how to print any Cat to the console
    * and a Printer[Animal] knows how to print any Animal to the console
    * it makes sense that a Printer[Animal] would also know how to print any Cat
    * The inverse relationship does not apply, because a Printer[Cat] does not know how to print any Animal to the console
    * Therefore, we should be able to substitute a Printer[Animal] for a Printer[Cat], if we wish, and making Printer[A] contravariant allows us to do exactly that.
    */
  abstract class Printer[-A] {
    def print(value: A): Unit
  }
  class AnimalPrinter extends Printer[Animal] {
    def print(animal: Animal): Unit = println("The animal's name is: " + animal.name)
  }
  class CatPrinter extends Printer[Cat] {
    def print(cat: Cat): Unit = println("The cat's name is: " + cat.name)
  }
  val myCat: Cat = Cat("Contravariant CAT")
  def printMyCat(printer: Printer[Cat]): Unit = {
    printer.print(myCat)
  }
  val catPrinter: Printer[Cat] = new CatPrinter
  val animalPrinter: Printer[Animal] = new AnimalPrinter

  printMyCat(catPrinter)
  printMyCat(animalPrinter)

  /**
    * invariance
    * Generic classes in Scala are invariant by default
    */
  class Container[A](value: A) {
    private var _value: A = value
    def getValue: A = _value
    def setValue(value: A): Unit = { _value = value }
  }
  val catContainer: Container[Cat] = new Container(Cat("Felix"))
//  val animalContainer: Container[Animal] = catContainer // will get mistake here because of invariance
//  animalContainer.setValue(Dog("Spot"))
//  val cat: Cat = catContainer.getValue
}
