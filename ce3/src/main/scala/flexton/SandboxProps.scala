package flexton

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class SandboxProps extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  test("1") {

    def add(a: Int, b: Int): Int = a + b

    val table = Table(
      ("in1", "in2", "out"),
      (0, 1, 1),
      (1, 1, 2),
      (5, 6, 11)
    )

    forAll(table) { (a, b, c) =>
      add(a, b) shouldBe c
    }

  }

  test("2") {

    val genInt = Gen.posNum[Int]
    implicit val arbInt: Arbitrary[Int] = Arbitrary(genInt)
    forAll { (x: Int, y: Int) =>
      x + y should be >= x
      x + y should be >= y
    }

  }

}
