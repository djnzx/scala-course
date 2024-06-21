package djnzx.features3

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object HandlingPartial {

  sealed trait Animal
  case object Mice extends Animal
  case object Cat  extends Animal
  case object Dog  extends Animal

}

class HandlingPartial extends AnyFunSuite with Matchers {

  import HandlingPartial.*

  /** partial !!! */
  def parse1(raw: String): Animal = raw match
    case "cat" => Cat
    case "dog" => Dog

  // TODO
  test("handling partial") {

    /** partial !!! */
    parse1("cat") match
      case Cat => println("cat!")

    parse1("blah") match
      case Mice => pprint.log("Mice parsed")

  }

}
