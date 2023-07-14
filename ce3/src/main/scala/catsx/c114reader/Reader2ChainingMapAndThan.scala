package catsx.c114reader

import cats.data.Reader
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Reader2ChainingMapAndThan {

  val f1 = (a: Int) => a + 1
  val f2 = (a: Int) => a * 2

  val r1: Reader[Int, Int] = Reader(f1)
  val r2: Reader[Int, Int] = Reader(f2)

  /** we can chain Reader and function */
  val r12a: Reader[Int, Int] = r1 map f2

  /** we can combine two Readers */
  val r12b: Reader[Int, Int] = r1 andThen r2

  val r12c: Reader[Int, (Int, Int)] = for {
    a1 <- r1
    a2 <- r2
  } yield (a1, a2)

}

class Reader2ChainingMapAndThanSpec extends AnyFunSpec with Matchers {

  describe("0") {

    import Reader2ChainingMapAndThan._

    it("1") {
      r12a.apply(10) shouldBe 22
    }

    it("2") {
      r12b.apply(10) shouldBe 22
    }

    it("3") {
      r12c.apply(10) shouldBe (11, 20)
    }

  }

}
