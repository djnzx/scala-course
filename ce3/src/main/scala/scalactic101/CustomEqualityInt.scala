package scalactic101

import org.scalactic.Equality
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object CustomEqualityInt {

  implicit val intDifferenceLeeThan10: Equality[Int] = new Equality[Int] {
    override def areEqual(a1: Int, b: Any): Boolean = b match {
      case a2: Int => math.abs(a1 - a2) < 10
      case _       => false
    }
  }

}

class CustomEqualityIntSpec extends AnyFunSpec with Matchers {

  describe("custom int equality") {
    it("equalities") {
      import CustomEqualityInt._
      1 shouldEqual 10
      1 should equal(10)
    }
  }

}