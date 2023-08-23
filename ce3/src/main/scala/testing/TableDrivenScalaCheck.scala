package testing

import cats.implicits._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.derive.DerivedInstances
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class TableDrivenScalaCheck extends AnyFunSpec with Matchers with ScalaCheckPropertyChecks with DerivedInstances {

  case class Person(id: Int, name: String)

  it("1") {
    implicit val ai: Arbitrary[Int]    = Arbitrary(Gen.choose(18, 75))
    implicit val as: Arbitrary[String] = Arbitrary(
      Gen.oneOf("Jim", "Bim", "Jack", "Daniel", "Alex", "Sergio", "Jeremy")
//      Gen.alphaStr
//      Gen.stringOfN(20, Gen.alphaChar))
    )
    forAll { (xs: List[Option[Person]]) =>
      pprint.pprintln(xs)
      xs should contain allElementsOf xs.flatten.map(_.some)
    }
  }

  it("2") {
    val badCombinations = Table(
      ("n", "d"),
      (Integer.MIN_VALUE, Integer.MIN_VALUE),
      (1, Integer.MIN_VALUE),
      (Integer.MIN_VALUE, 1),
      (Integer.MIN_VALUE, 0),
      (1, 0)
    )

    class Fraction(n: Int, d: Int) {
      require(d != 0)
      require(d != Integer.MIN_VALUE)
      require(n != Integer.MIN_VALUE)
      val numer = if (d < 0) -1 * n else n
      val denom = d.abs

      override def toString = numer + " / " + denom
    }

    forAll(badCombinations) { (n: Int, d: Int) =>
      an[IllegalArgumentException] should be thrownBy {
        new Fraction(n, d)
      }

      val x = the[IllegalArgumentException] thrownBy {
        new Fraction(n, d)
      }
      x.getMessage shouldBe "requirement failed"

    }
  }
}
