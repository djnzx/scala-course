package json.trivial

import app.model.Relation
import cats.Id
import cats.implicits.catsSyntaxEitherId
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import zio.json.DecoderOps

class ContainersDecodeTest extends AnyFunSuite with Matchers with Inside {

  test("RAW.deserialize - List[Int]") {
    "[1,2,3,3]".fromJson[List[Int]] shouldBe List(1, 2, 3, 3).asRight
  }

  test("RAW.deserialize - Vector[Int]") {
    "[1,2,3,3]".fromJson[Vector[Int]] shouldBe Vector(1, 2, 3, 3).asRight
  }

  test("RAW.deserialize - Set[Int]") {
    "[1,2,3,3]".fromJson[Set[Int]] shouldBe Set(1, 2, 3).asRight
  }

  test("RAW.deserialize - Set[Relation]") {
    """["any", "any", "all"]""".fromJson[Set[Relation]] shouldBe
      Set[Relation](Relation.All, Relation.Any).asRight
  }

  test("RAW.deserialize - Id[Int]") {
    inside("1".fromJson[Id[Int]]) {
      case Right(x) => x shouldBe 1
    }
  }

  test("RAW.deserialize - Id[Double") {
    inside("1.23".fromJson[Id[Double]]) {
      case Right(x) => x shouldBe 1.23
    }
  }

  test("RAW.deserialize - Id[BigDecimal]") {
    inside("12.34".fromJson[Id[BigDecimal]]) {
      case Right(x) => x shouldBe BigDecimal(12.34)
    }
  }

}
