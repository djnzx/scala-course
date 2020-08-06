package fp_red.red13

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TailRecPlaygroundSpec extends AnyFunSpec with Matchers {

  describe("tailrec") {
    import TailRecPlayground._

    it("1") {
      runEven(     0) shouldEqual true
      runEven(   101) shouldEqual false
      runEven( 1_101) shouldEqual false
      runEven(10_101) shouldEqual false
    }
    it("2") {
      runEven(    100_000) shouldEqual true
      runEven(  1_000_001) shouldEqual false
      runOdd ( 10_000_000) shouldEqual false
    }
    it("3") {
      runOdd (100_000_001) shouldEqual true
    }

  }

}
