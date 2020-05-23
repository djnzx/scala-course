package cookbook.x006

object FactoryAppInt extends App {
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

  object Animal {
    def apply(aType: Int): Animal = aType match {
      case 1 => new Dog
      case 2 => new Cat
      case 3 => new Leo
//      case _ => new Animal {
//        override def sound = "_unknown"
//      }
      case _ => throw new Exception("_unknown animal given")
    }
  }

  val a1 = Animal(1)
  val a2 = Animal(2)
  val a3 = Animal(3)
  var a4: Option[Animal] = None
  try {
    a4 = Some(Animal(4))
  } catch {
    case e: Exception => println(e.getMessage)
  }
  println(a1.getClass)
  println(a2.getClass)
  println(a3.getClass)
  a4.foreach(_.sound)
}
