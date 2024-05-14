package json

import app.api.{Predicate, PropVal}
import app.model.Relation
import cats.implicits.catsSyntaxEitherId
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableFor2
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import zio.json.{DecoderOps, EncoderOps}

class EnumsEncodeDecodeTest extends AnyFunSuite with Matchers with Inside
    with ScalaCheckPropertyChecks {

  test("PropVal.serialize / deserialize") {

    val testData: TableFor2[PropVal, String] = Table(
      "obj" -> "json",
      PropVal.SportId -> """
                           |"SportId"
                           |""".stripMargin.trim,
      PropVal.TypeId -> """
                          |"TypeId"
                          |""".stripMargin.trim
    )

    forAll(testData) { case (obj, json) =>
      obj.toJson shouldBe json
      json.fromJson[PropVal] shouldBe obj.asRight
    }

  }

  test("PropVal.deserialize failure") {

    """
        |"blah"
        |""".stripMargin.trim.fromJson[PropVal].toOption shouldBe empty

  }

  test("Predicate.serialize / deserialize") {

    val testData: TableFor2[Predicate, String] = Table(
      "obj" -> "json",
      Predicate.eq -> """
                        |"eq"
                        |""".stripMargin.trim,
      Predicate.notin -> """
                           |"notin"
                           |""".stripMargin.trim
    )

    forAll(testData) { case (obj, json) =>
      obj.toJson shouldBe json
      json.fromJson[Predicate] shouldBe obj.asRight
    }

  }

  test("Predicate.deserialize failure") {

    """
      |"blah"
      |""".stripMargin.trim.fromJson[Predicate].toOption shouldBe empty

  }

  test("Relation.serialize / deserialize") {

    val testData: TableFor2[Relation, String] = Table(
      "obj" -> "json",
      Relation.Any -> """
                        |"any"
                        |""".stripMargin.trim,
      Relation.All -> """
                        |"all"
                        |""".stripMargin.trim
    )

    forAll(testData) { case (obj, json) =>
      obj.toJson shouldBe json
      json.fromJson[Relation] shouldBe obj.asRight
    }

  }

  test("Relation.deserialize failure") {

    """
      |"blah"
      |""".stripMargin.trim.fromJson[
      Relation
    ] shouldBe "(Relation: no such member `blah`)".asLeft

  }

}
