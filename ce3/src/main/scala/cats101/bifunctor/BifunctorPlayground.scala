package cats101.bifunctor

import cats.implicits._
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class BifunctorPlayground extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  test("Either") {

    val e1: Either[String, Int] = "10.5".asLeft[Int]
    val e2: Either[String, Int] = 15.asRight[String]

    e1.bimap(_.toDoubleOption, _.toString) shouldBe 10.5.some.asLeft
    e2.bimap(_.toDoubleOption, _.toString) shouldBe "15".asRight
  }

}
