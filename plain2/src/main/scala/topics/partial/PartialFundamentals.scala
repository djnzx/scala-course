package topics.partial

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object PartialFundamentals {

  val f1: PartialFunction[Int, String] = {
    case 1 => "one"
    case 2 => "two"
  }

  val f2: PartialFunction[Int, String] = {
    case 3 => "three"
    case 4 => "four"
  }

}

class PartialFundamentals extends AnyFunSuite with Matchers {

  import PartialFundamentals._

  /** it looks like a normal function, when defined */
  test("partial - handled") {
    f1(1) shouldBe "one"
    f1(2) shouldBe "two"
  }

  /** it throws an exception, when not defined */
  test("partial - non-handled") {
    an[MatchError] shouldBe thrownBy(f1(3))
  }

  /** it has an api to check whether defined */
  test("partial - isDefinedAt") {
    f1.isDefinedAt(1) shouldBe true
    f1.isDefinedAt(3) shouldBe false
  }

  /** can be lifted to {{{A => Option[B]}}} */
  test("partial - lifted") {
    val f1a: Int => Option[String] = f1.lift

    f1a(1) shouldBe Some("one")
    f1a(3) shouldBe None
  }

  /** {{{A => Option[B]}}} can be unlifted to become partial again */
  test("partial - unlifted") {
    val f1a: Int => Option[String] = f1.lift
    val f1b: PartialFunction[Int, String] = f1a.unlift

    f1b(1) shouldBe "one"
    an[MatchError] shouldBe thrownBy(f1b(3))
  }

  test("partial - composed") {
    val f3 = f1 orElse f2
    f3(1) shouldBe "one"
    f3(3) shouldBe "three"
    an[MatchError] shouldBe thrownBy(f3(5))
  }

  class Dog
  case class Shepherd() extends Dog
  case class Laika()    extends Dog
  // orElse[A1 <: A, B0 >: B](that: PartialFunction[A1, B0]): PartialFunction[A1, B0]
  // B0 - is the most common type

  test("partial - covariant in the result position") {
    val f1: PartialFunction[Int, Shepherd] = {
      case 1 => Shepherd()
    }

    val f2: PartialFunction[Int, Laika] = {
      case 2 => Laika()
    }

    // during orElse Laika is treated as a Dog
    // and whole result "widened" to Dog
    val f3: PartialFunction[Int, Dog] = f1 orElse f2

    f3(1) shouldBe Shepherd()
    f3(2) shouldBe Laika()
  }

  test("partial - contravarint in the argument position - consuming type is proper but absurd") {
    trait A
    case class A1() extends A
    case class A2() extends A

    val f1: PartialFunction[A1, Int] = {
      case A1() => 1
    }
    val f2: PartialFunction[A2, Int] = {
      case A2() => 2
    }
    // if they are different leafs => we end up with a type requires both
    // so we need to be both types at the same moment ))
    val f3: PartialFunction[A2 with A1, Int] = f1 orElse f2
  }

  test("partial - contravarint in the argument position - 2") {
    trait A0
    trait A1 extends A0

    val f0: PartialFunction[A0, Int] = _ => 1
    val f1: PartialFunction[A1, Int] = _ => 2

    // if consuming parameters on the same hierarchy
    // as a result the most special type will be picked
    val f2: PartialFunction[A1, Int] = f0 orElse f1
  }

}
