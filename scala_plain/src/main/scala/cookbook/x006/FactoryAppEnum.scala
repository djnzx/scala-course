package cookbook.x006

object FactoryAppEnum extends App {
  trait Animal {
    def sound: String
  }
  class Dog extends Animal {
    override def sound = "bark"
  }
  class Cat extends Animal {
    override def sound = "meow"
  }
  class Leo extends Animal {
    override def sound = "roar"
  }

  object Animal extends Enumeration(1) {
    val Dog, Cat, Leo = Value
//    type AnimalType = Value
//    def apply(aType: AnimalType): Animal = aType match {
    def apply(aType: Animal.Value) = aType match {
      case Animal.Dog => new Dog
      case Animal.Cat => new Cat
      case Animal.Leo => new Leo
      case _ => throw new Exception("_unknown animal given")
    }
  }

  val a11 = Animal(Animal.Dog)
  val a21 = Animal(Animal.Cat)
  val a31 = Animal(Animal.Leo)
  println(a11.getClass)
  println(a21.getClass)
  println(a31.getClass)
  val values: Enumeration#ValueSet = Animal.values // nested classes declared this way
  println(values.getClass)
}
