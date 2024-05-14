package json.trivial

import cats.implicits.catsSyntaxEitherId
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import zio.json.DecoderOps

class PlainValuesDecodeTest extends AnyFunSuite with Matchers with Inside {

  test("Int") {
    "1".fromJson[Int] shouldBe 1.asRight
  }

  test("Double") {
    "1.23".fromJson[Double] shouldBe 1.23.asRight
  }

  test("BigDecimal") {
    "1.2345".fromJson[BigDecimal] shouldBe BigDecimal(1.2345).asRight
  }

}
