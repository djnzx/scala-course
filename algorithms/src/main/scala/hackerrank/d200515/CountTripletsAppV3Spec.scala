package hackerrank.d200515

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CountTripletsAppV3Spec extends AnyFunSpec with Matchers {

  describe("triplets") {
    //                        0 1 2 3  4  5
    val SRC = Array(1,2,3,4,15,20)
    
    it("number of indexes >= from") {
      import CountTripletsAppV3.countFrom3
      
      countFrom3(SRC, 0) shouldEqual 6
      countFrom3(SRC, 1) shouldEqual 6
      countFrom3(SRC, 2) shouldEqual 5

      countFrom3(SRC, 5) shouldEqual 2
      countFrom3(SRC, 20) shouldEqual 1
      countFrom3(SRC, 21) shouldEqual 0
      countFrom3(SRC, 50) shouldEqual 0
    }
    
    it("base index from given") {
      import CountTripletsAppV3.findFirstFrom2
      findFirstFrom2(SRC, 0) shouldEqual Some(0)
      findFirstFrom2(SRC, 1) shouldEqual Some(0)
      findFirstFrom2(SRC, 2) shouldEqual Some(1)
      findFirstFrom2(SRC, 4) shouldEqual Some(3)
      findFirstFrom2(SRC, 5) shouldEqual Some(4)
      findFirstFrom2(SRC, 15) shouldEqual Some(4)
      findFirstFrom2(SRC, 20) shouldEqual Some(5)
      findFirstFrom2(SRC, 21) shouldEqual None
      findFirstFrom2(SRC, 50) shouldEqual None
    }
  }
}
