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

}
