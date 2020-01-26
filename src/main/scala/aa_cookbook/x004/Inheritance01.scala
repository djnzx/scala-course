package aa_cookbook.x004

object Inheritance01 extends App {

  abstract class Base {
    val name: String
  }

  class Derived extends Base {
    // at this point override not necessary because of real absence `name` field
    override val name = "Name implemented in Derived"
  }

  class DerivedNext extends Derived {
    override val name = "Name overridden in DerivedNext"
  }

  val d0 = new Base {
    override val name: String = "Implemented in the body"
  }
  println(d0.name)

  val d1 = new Derived
  println(d1.name)

  val d2 = new DerivedNext
  println(d2.name)


}
