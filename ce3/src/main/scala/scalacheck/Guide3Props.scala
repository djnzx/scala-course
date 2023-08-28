package scalacheck

import org.scalacheck.Prop.forAll
import org.scalacheck.Prop
import org.scalacheck.Properties
import org.scalacheck.util.Pretty.Params
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.Checkers
//import scalacheck.PropsStringOperation.property

/** we define properties
  *   - they will be checked automatically
  *   - data will be generated automatically (randomly with corner cases, or by our custom logic)
  *   - tests will be run in parallel
  *   - these are descriptions!
  */

/** scalacheck integration with scalatest */
class PropsStringOperationSpec extends AnyFunSpec with Checkers {
  describe("scalacheck integration") {
    it("test case 1") {
      check(
        (a: String, b: String) => (a + b).startsWith(a),
        minSuccessful(500),
      )
    }
  }
}

/** plain scalacheck */
object PropsStringOperation extends Properties("String some methods") {

  property("string: startsWith") = forAll { (a: String, b: String) =>
    (a + b).startsWith(a)
  }

  property("string: concatenate") = forAll { (a: String, b: String) =>
    val ab = a + b

    ab.length >= a.length &&
    ab.length >= b.length
  }

  property("string: substring") = forAll { (a: String, b: String, c: String) =>
    (a + b + c).substring(a.length, a.length + b.length) == b
  }

}

object PropertiesIntegerAddition extends Properties("calc.add.assoc.prop") {

  property("int add: zero element") = forAll { (a: Int) =>
    a + 0 == a &&
    0 + a == a
  }

  property("int add: commutativity") = forAll { (a: Int, b: Int) =>
    a + b == b + a
  }

  property("int add: associativity") = forAll { (a: Int, b: Int, c: Int) =>
    (a + b) + c == a + (b + c)
  }

}
