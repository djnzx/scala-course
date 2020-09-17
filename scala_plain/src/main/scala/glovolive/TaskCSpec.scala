package glovolive

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TaskCSpec extends AnyFunSpec with Matchers {
  describe("task C") {
    import 
//    TaskCBruteForce._
//    TaskCDynamic._
    TaskCDynamicScala._
    
    it("containsZeroSubArray") {
      Map(
        Array(0) -> true,
        Array(-10, 10) -> true,
        Array(-10, 10, 1, 2, 3) -> true,
        Array(-90,50,-10, 10, 1, 2, 3) -> true,
        Array(-3,1,2,3) -> true,
        Array(3,-1,-4,0,5,6,7) -> true,
        Array(3,-1,-4,0,4,8,9) -> true,
        Array[Int]() -> false,
        Array(1,2,3) -> false,
        Array(-3,-2,-2,1,2,3,-1) -> false,
      ).foreach { case (a, exp) => 
        containsZeroSubArray(a) shouldEqual exp
      }
    }
    
    it("hasSubArrayWithSum") {
      Map(
        (Array( 3, 4, 7, 2, -3, 1, 4, 2, 7), 7) -> 5,
        (Array(-7, 1, 2, 3,10), 6)              -> 1,
        (Array(-7, 1, 1, 2, 3, 10), 0)          -> 1,
        (Array( 3,-1,-4, 0, 1, 3, 5), 0)        -> 2,
      ).foreach { case ((a, k), exp) =>
        hasSubArrayWithSum(a, k) shouldEqual exp
      }
    }
    
  }
}
