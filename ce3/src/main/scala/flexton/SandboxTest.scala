package flexton

import enumeratum.CatsEnum
import enumeratum.CirceEnum
import enumeratum.CogenInstances
import enumeratum.DoobieEnum
import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Lowercase
import enumeratum.ScalacheckInstances
import io.circe.Codec
import io.circe.generic.AutoDerivation
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec
import io.circe.syntax.EncoderOps
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.Inside
import org.scalatest.Succeeded
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scalacheck.genextra.GenExtraSyntax

object enumx {
  sealed trait Color extends EnumEntry with Lowercase
  object Color
      extends Enum[Color]
      with CirceEnum[Color]    // Encoder[A] + Decoder[A]
      with CatsEnum[Color]     // Show[A] + Hash[A] + Eq[A]
      with ScalacheckInstances // arbEnumEntry[EnumType <: EnumEntry] +
      with CogenInstances {

    case object Red    extends Color
    case object Yellow extends Color
    case object Green  extends Color

    override def values: IndexedSeq[Color] = findValues
  }
}

class SandboxTest extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside with GenExtraSyntax {

  object domain {
    case class Age(value: Int)
    object Age {
      implicit val codec: Codec[Age] = deriveUnwrappedCodec
    }

    case class Name(value: String)
    object Name {
      implicit val codec: Codec[Name] = deriveUnwrappedCodec
    }

    case class Data(name: Name, age: Age)
    object Data extends AutoDerivation

    val genAge: Gen[Age] = Gen.choose(18, 70).map(Age.apply)
    val genName: Gen[Name] = Gen.oneOf("Jim", "Sergio", "Alex", "Bill", "Nate").map(Name.apply)

    val genData: Gen[Data] = for {
      name <- genName
      age  <- genAge
    } yield Data(name, age)
  }

  object domain2 {
    import enumx._

    case class Name(value: String)
    object Name {
      implicit val codec: Codec[Name] = deriveUnwrappedCodec
    }

    case class Data(name: Name, color: Color)
    object Data extends AutoDerivation

    val genName: Gen[Name] = Gen.oneOf("Jim", "Sergio", "Alex", "Bill", "Nate").map(Name.apply)
    val genColor: Gen[Color] = implicitly[Arbitrary[Color]].arbitrary

    implicit val genData: Gen[Data] = for {
      name <- genName
      c  <- genColor
    } yield Data(name, c)
  }

  import domain._

  val genList: Gen[List[Data]] = genData.listOfN(20)

  test("enum") {
    import enumx._
    import domain2._

    val xs = implicitly[Gen[Data]]
      .listOfN(5)
      .sample
      .get

    xs
      .map { x => pprint.pprintln(x); x }
      .map { x =>
        val rawJson = x.asJson.noSpaces
        println(s"json: $rawJson")
        val c = io.circe.parser.decode[Data](rawJson).getOrElse(???)
        println(s"parsed: $c")
        x
      }
//      .map { x => pprint.pprintln(x.entryName); x }
//      .map {}

    Succeeded
  }

  test("unwrapped") {
    val raw =
      """
        |3
        |""".stripMargin
    val x = io.circe.parser.decode[Int](raw)
    pprint.pprintln(x)
  }

  test("2") {
    val raw =
      """
        |{
        |  "name" : "Jim",
        |  "age" : 33
        |}
        |""".stripMargin

    inside(io.circe.parser.decode[Data](raw)) { case Right(x) =>
      pprint.pprintln(x)
      x shouldBe Data(
        name = Name(value = "Jim"),
        age = Age(value = 33)
      )
    }
  }

  test("1") {
    val xs = genList.sample.get

    xs.foreach(x => pprint.pprintln(x.asJson))
    Succeeded
  }

}
