package genextra

import org.scalacheck.Gen
import org.scalatest.Succeeded
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ZipIdeas extends AnyFunSuite
  with Matchers
  with ScalaCheckPropertyChecks
  with GenExtraSyntax
  with ArbitraryFromGen
  with ValueClassesOps
  with PrettyPrint
  with MkInfinite
{

  case class CarId(value: String) extends Value[String]
  val genBrandId: Gen[CarId] = Gen.prefixedStringOfN(6, "car-").map(CarId)
  implicit val genNCars: Gen[List[CarId]] = genBrandId.listOfN(3)
  implicit val genNNumbers: Gen[List[Int]] = Gen.choose(1000, 9999).listOfN(50)

  test("zip sequences into tuple") {
    // since we know that brands.size << numbers.size
    forAll { (brandIds: List[CarId], numbers: Seq[Int]) =>
      pprint.pprintln(numbers, width = 1000)
      pprintln(brandIds.size -> numbers.size)
      // we can do zip
      val pairs = brandIds.infinite zip numbers
      pairs.foreach(x => pprintln(x))
      Succeeded
    }
  }

}
