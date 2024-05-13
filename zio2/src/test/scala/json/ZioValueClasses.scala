package json

import cats.Id
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import zio.json.{DecoderOps, EncoderOps}

class ZioValueClasses extends AnyFunSuite with Matchers with Inside with ScalaCheckPropertyChecks {

  test("Value.serialize - Id[Int]") {
    val x    = Value(Id(1))
    val json = x.toJson
    pprint.log(json)
    json shouldBe "{\"value\":1}"
  }

  test("Value.serialize - List[Int]") {
    val x    = Value(List(1, 2, 3))
    val json = x.toJson
    pprint.log(json)
    json shouldBe "{\"value\":[1,2,3]}"
  }

  test("Value.deserialize - Id[Int]") {
    inside("{\"value\":1}".fromJson[Value[Int, Id]]) {
      case Right(x) =>
        x shouldBe Value(Id(1))
        x shouldBe Value[Int, Id](1)
    }
  }

  test("Value.deserialize - List[Int]") {
    inside("{\"value\":[1,2,3]}".fromJson[Value[Int, List]]) {
      case Right(x) =>
        x shouldBe Value(List(1, 2, 3))
    }
  }


}
