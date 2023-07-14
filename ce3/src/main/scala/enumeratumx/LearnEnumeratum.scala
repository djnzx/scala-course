package enumeratumx

import cats.implicits.catsSyntaxEitherId
import enumeratum.EnumEntry.Lowercase
import enumeratum.EnumEntry.Snakecase
import enumeratum.EnumEntry.Uppercase
import enumeratum._
import io.circe.syntax.EncoderOps
import org.scalactic.Equality
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object LearnEnumeratum {

  /** 1. extend EnumEntry + naming rule:
    * CapitalSnakecase
    * CapitalHyphencase
    * CapitalDotcase
    * CapitalWords
    *
    * Camelcase
    * Uppercase
    * Lowercase
    * Uncapitalised
    *
    * Snakecase        = CapitalSnakecase  + Lowercase
    * UpperSnakecase   = CapitalSnakecase  + Uppercase
    * Hyphencase       = CapitalHyphencase + Lowercase
    * UpperHyphencase  = CapitalHyphencase + Uppercase
    * Dotcase          = CapitalDotcase    + Lowercase
    * UpperDotcase     = CapitalDotcase    + Uppercase
    * Words            = CapitalWords      + Lowercase
    * UpperWords       = CapitalWords      + Uppercase
    * LowerCamelcase   = Camelcase         + Uncapitalised
    */
  sealed trait Color extends EnumEntry with Lowercase with Snakecase
//    with Uppercase

  /** 2. object extends Enum[A] */

  object Color
      extends Enum[Color]
      with CirceEnum[Color]    // Encoder[A] + Decoder[A]
      with DoobieEnum[Color]   // Meta[A]
      with CatsEnum[Color]     // Show[A] + Hash[A] + Eq[A]
      with ScalacheckInstances // arbEnumEntry[EnumType <: EnumEntry] +
      with CogenInstances      // cogenEnumEntry[EnumType <: EnumEntry: Enum]
      {
    override def values: IndexedSeq[Color] = findValues

    case object Red extends Color
    case object Green extends Color
    case class Blue(level: Int) extends Color
  }

}

object Equalities {
  implicit val intDifferenceLeeThan10: Equality[Int] = new Equality[Int] {
    override def areEqual(a1: Int, b: Any): Boolean = b match {
      case a2: Int => math.abs(a1 - a2) < 10
      case _       => false
    }
  }
}

class LearnEnumeratumSpec extends AnyFunSpec with Matchers {
  import LearnEnumeratum._

  describe("1") {
    it("equalities") {
      import Equalities._
      1 shouldEqual 10
      1 should equal(10)
    }
    it("1") {

      val q         = '"'
      val c1: Color = Color.Red
      val c2: Color = Color.Blue(3)
      val n1        = "red"
      val n2        = "blue(3)"
      val j1        = q + n1 + q
      val j2        = q + n2 + q

      c1.entryName shouldBe n1
      c2.entryName shouldBe n2
      io.circe.parser.decode[Color](j1) shouldBe c1.asRight
      // enums deserialization for case classes doesn't work
//      println(c1.entryName)
//      println(c2.entryName)
//      println(c1.asJson)
//      println(c2.asJson)
//      pprint.pprintln(
//        io.circe.parser.decode[Color](j1)
//      )
//      pprint.pprintln(
//        io.circe.parser.decode[Color](j2)
//      )
    }
  }
}
