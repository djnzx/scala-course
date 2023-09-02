package scalacheck.extrasyntax

import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class GenExtraSyntaxTest extends BootstrapFunSuite with ScalaCheckDrivenPropertyChecks with ArbitraryFromGen with GenExtraSyntax {

  override implicit val generatorDrivenConfig: PropertyCheckConfiguration = PropertyCheckConfiguration(minSuccessful = 25)

  val tenCharsId = Gen.listOfN(10, Gen.alphaChar).map(_.mkString)

  test(".const syntax provides always constant value") {
    implicit val g: Gen[String] = tenCharsId.const

    forAll { (x: String) =>
      pprint.pprintln(x)
      x shouldBe g.sample.value
    }
  }

  test(".constListOfN(...) syntax provides always constant value of a List[A]") {
    implicit val g: Gen[List[String]] = tenCharsId.constListOfN(5)

    forAll { (x: List[String]) =>
      pprint.pprintln(x)
      x shouldBe g.sample.value
    }
  }

  test(".oneOf syntax provides always constant value from the Gen[Geq[A]] given") {
    // always the same list
    val gs: Gen[List[String]] = tenCharsId.constListOfN(5)
    // element from the list
    implicit val g: Gen[String] = gs.oneOf

    forAll { (x: String) =>
      pprint.pprintln(x)
      gs.sample.value should contain(x)
    }

    pprint.pprintln(gs.sample.value)
  }

  test(".notPresentIn - allows to reuse generator, but with a guarantee not have a duplicate one") {

    /** Int: 1..10 */
    val g0: Gen[Int] = Gen.choose(1, 10)

    /** List[Int] max length = 9 */
    val g1: Gen[List[Int]] = Gen.listOfN(10, g0).map(_.distinct.sorted)

    /** Int: 1..10 but never in the list */
    val g2 = g0.notPresentIn _

    implicit val gtuple: Gen[(List[Int], Int)] = for {
      xs <- g1     // list
      x  <- g2(xs) // element not presented in the list
    } yield (xs, x)

    forAll { t: (List[Int], Int) =>
      t match {
        case (xs, x) =>
          pprint.pprintln((xs, x))
          xs shouldNot contain(x)
      }
    }

  }

}
