package json

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import zio.json._

class ZioPlainValuesEncode extends AnyFunSuite with Matchers with Inside {

  test("encode - Int") {
    1.toJson shouldBe "1"
  }

  test("encode - Double") {
    1.23.toJson shouldBe "1.23"
  }

  test("decode - BigDecimal") {
    BigDecimal(1.2345).toJson shouldBe "1.2345"
  }

}
