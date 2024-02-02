package scalacheck.genextra

import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scalacheck.WrappersIdeas

class ZipIdeas extends AnyFunSuite
  with Matchers
  with ScalaCheckPropertyChecks
  with GenExtraSyntax
  with ArbitraryFromGen
  with WrappersIdeas
  with PrettyPrint
  with MkInfinite
{

  case class CarId(value: String) extends Value[String]

  val genCar: Gen[CarId] = Gen.prefixedStringOfN(6, "car-").map(CarId)
  implicit val genNCars: Gen[List[CarId]] = genCar.listOfN(3)
  implicit val genNNumbers: Gen[List[Int]] = Gen.choose(1000, 9999).listOfN(50)

  test("zip sequences into tuple") {
    forAll { (as: List[CarId], bs: Seq[Int]) =>
      pprintln(as.size -> bs.size)
      pprint.pprintln(bs, width = 1000)
      val pairs = as.infinite zip bs
      pairs.foreach{x =>
        bs should contain (x._2)
        as should contain (x._1)
        pprintln(x)
      }
    }
  }

}
