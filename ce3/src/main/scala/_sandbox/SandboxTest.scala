package _sandbox

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Inside, Succeeded}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class SandboxTest extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  test("1") {
    Succeeded
  }

}
