package json.trivial

import app.model.PropVal.SportId
import cats.implicits.catsSyntaxEitherId
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import zio.json.{DecoderOps, EncoderOps}

class ValueClassEncodeDecode extends AnyFunSuite with Matchers with Inside
    with ScalaCheckPropertyChecks {

  test("SportId - encode") {
    SportId(123).toJson shouldBe "123"
  }

  test("SportId - decode") {
    "123".fromJson[SportId] shouldBe SportId(123).asRight
  }

}
