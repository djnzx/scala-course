package scalacticx

import org.scalactic.NormMethods.convertToNormalizer
import org.scalactic._
import org.scalactic.StringNormalizations._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Intro01Spec extends AnyFunSpec with Matchers {

  val c1: NormalizingEquality[String] = after being lowerCased
  val c2: NormalizingEquality[String] = after being upperCased
  val c3: NormalizingEquality[String] = after being trimmed

  it("String - lowerCased") {
    ("Hello" shouldEqual "hello")(c1)
  }

  it("String - UpperCased") {
    ("HeLLo" shouldEqual "HELLO")(c2)
  }

  it("String - Trimmed") {
    ("123" shouldEqual " 123 \n\t")(c3)
  }

  it("norm 1") {
    implicit val c: Uniformity[String] = lowerCased
    "WHISPER".norm shouldEqual "whisper"
  }

  it("equality 1") {
    implicit val eq: NormalizingEquality[String] = decided by defaultEquality[String] afterBeing lowerCased

    "Hello" shouldEqual "hello"
    "normalized" shouldEqual "NORMALIZED"
  }

  it("tolerance 1") {
    2.00001 shouldEqual 2.0 +- 0.01
  }

  it("tolerance 2") {
    import TolerantNumerics._

    implicit val dblEquality: Equality[Double] = tolerantDoubleEquality(0.01)

    2.00001 shouldEqual 2.0
  }

}
