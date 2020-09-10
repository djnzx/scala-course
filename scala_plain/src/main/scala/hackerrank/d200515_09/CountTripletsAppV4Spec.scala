package hackerrank.d200515_09

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CountTripletsAppV4Spec extends AnyFunSpec with Matchers {

  describe("triplets") {
    //              0 1 2 3  4  5
    val SRC = Array(1,2,3,4,15,20)
    
    it("toL") {
      import CountTripletsAppV4.toL
      
      toL(SRC, 1) shouldEqual 1
      toL(SRC, 2) shouldEqual 2
      toL(SRC, 3) shouldEqual 3
      toL(SRC, 4) shouldEqual 4
      toL(SRC, 5) shouldEqual 4
      toL(SRC, 10) shouldEqual 4
      toL(SRC, 15) shouldEqual 5
      toL(SRC, 20) shouldEqual 6
      toL(SRC, 30) shouldEqual 6
    }
    
    it("toR") {
      import CountTripletsAppV4.toR
      toR(SRC, 0) shouldEqual 6
      toR(SRC, 1) shouldEqual 6
      toR(SRC, 2) shouldEqual 5
      toR(SRC, 3) shouldEqual 4
      toR(SRC, 4) shouldEqual 3
      toR(SRC, 5) shouldEqual 2
      toR(SRC, 10) shouldEqual 2
      toR(SRC, 15) shouldEqual 2
      toR(SRC, 20) shouldEqual 1
      toR(SRC, 30) shouldEqual 0
    }
    
    it("whole") {
      import CountTripletsAppV4.countTriplets
      
      Map(
        (Array[Long](1,2,2,4), 2) -> 2,
        (Array[Long](1, 3, 9, 9, 27, 81), 3) -> 6
      )
        .foreach { case ((a, r), exp) => 
        countTriplets(a, r) shouldEqual exp
      }
    }
  }
}
