package testing

import cats.Eq
import cats.implicits.catsSyntaxEitherId
import io.circe.testing.ArbitraryInstances
import io.circe.testing.CodecTests
import io.circe.syntax._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class EncoderDecoderSpec extends AnyFunSpec with Matchers with ScalaCheckPropertyChecks {

  object Implicits extends ArbitraryInstances {
    implicit val eqPerson: Eq[Person] = Eq.fromUniversalEquals
    implicit val arbPerson: Arbitrary[Person] = Arbitrary {
      Gen.listOf(Gen.alphaChar) map { chars => Person(chars.mkString("")) }
    }
  }

  it("1") {
    forAll(Gen.posNum[Int]) { x: Int =>
      x shouldEqual   1000
    }
  }

  it("test plain") {
    import Implicits._

    forAll { p: Person =>
      p.asJson.as[Person] shouldEqual p.asRight
    }
  }

  it("framework provided approach - min") {
    import Implicits._

    CodecTests[Person]
      .unserializableCodec
      .all
      .check()
  }

  it("framework provided approach - full") {
    import Implicits._

    CodecTests[Person]
      .codec
      .all
      .check()
  }

}
