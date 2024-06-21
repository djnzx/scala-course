package djnzx.features3

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object TransparentTraitFeature {

  sealed trait Animal
  case object Mice extends Animal
  case object Cat  extends Animal
  case object Dog  extends Animal

}

class TransparentTraitFeature extends AnyFunSuite with Matchers {

  import TransparentTraitFeature.*

  test("1") {
    def parse2(raw: String): Cat.type | Dog.type = raw match
      case "cat" => Cat
      case "dog" => Dog

    /** scala3 union type behavior */
    parse2("cat") match
      case Cat => println("cat!")
      case Dog => ??? // warning here!
//      case Mice => ??? // pattern type is incompatible with expected type
//      case _   => ???

    transparent sealed trait CanFly
    case object Airplane   extends CanFly
    case object Helicopter extends CanFly
    case object Paraplane  extends CanFly

    def parse3(raw: String) = raw match
      case "airplane"   => Airplane
      case "helicopter" => Helicopter

    /** when we have `transparent` the default inference is Union Type */
    parse3("airplane") match
      case Airplane   => println("airplane!")
      case Helicopter => ???
    //    case Paraplane  => ???

  }

}
