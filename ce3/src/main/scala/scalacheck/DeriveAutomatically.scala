package scalacheck

import munit.FunSuite
import org.scalacheck.derive.DerivedInstances
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class DeriveAutomatically extends FunSuite with ScalaCheckDrivenPropertyChecks with DerivedInstances {

  case class Person(id: Int, name: String)

  test("1") {
    forAll { p: Person =>
      pprint.pprintln(p)
    }
  }

}
