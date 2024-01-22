package comonad

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class ComonadPlayground extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {
  import cats._
  import cats.data._
  import cats.implicits._

  test("non-empty list: pure / flatMap") {
    val fa = 1.pure[NonEmptyList]
    // List(1)

    val fb = fa.flatMap(x => NonEmptyList.of(-x, +x))
    // List(-1, 1)

    Seq(fa, fb)
      .map(_.toList)
      .foreach(x => pprint.pprintln(x))
  }

  test("non-empty list: extract") {
    val fa = NonEmptyList.of(1,2,3)
    val a = fa.extract

    Seq(fa.toList, a)
      .foreach(x => pprint.pprintln(x))
  }

  // https://typelevel.org/cats/typeclasses/comonad.html#:~:text=Comonad%20is%20a%20Functor%20and,depends%20on%20the%20particular%20type.
  test("non-empty list: coflatMap") {
    val x = NonEmptyList.of(1,2,3).coflatMap(identity)
    pprint.pprintln(x)
  }

  test("2") {

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

  test("3") {

    val genInt = Gen.posNum[Int]
    implicit val arbInt: Arbitrary[Int] = Arbitrary(genInt)
    forAll{ (x: Int, y: Int) =>
      x + y should be >= x
      x + y should be >= y
    }

  }

}
