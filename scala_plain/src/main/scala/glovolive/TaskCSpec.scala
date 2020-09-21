package glovolive

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class TaskCSpec extends AnyFunSpec with Matchers {
  describe("Task C") {
    
    it("containsZeroSubArray") {
      val t = List(
        Array(0),
        Array(-10, 10),
        Array(-10, 10, 1, 2, 3),
        Array(-90,50,-10, 10, 1, 2, 3),
        Array(-3,1,2,3),
        Array(3,-1,-4,0,5,6,7),
        Array(3,-1,-4,0,4,8,9),
      ).map(_->true).toMap
      
      val f = List(
        Array[Int](),
        Array(1,2,3),
        Array(-3,-2,-2,1,2,3,-1),
      ).map(_->false).toMap
      
      val impls = Seq(
        TaskCBruteForce.containsZeroSubArray _,
        TaskCDynamic.containsZeroSubArray _,
        TaskCDynamicScala.containsZeroSubArray _,
      )
      
      impls.foreach { impl =>
        (t ++ f).foreach { case (a, exp) =>
          impl(a) shouldEqual exp
        }
      }
      
    }
    
    it("hasSubArrayWithSum") {

      val data = Map(
        (Array( 3, 4, 7, 2, -3, 1, 4, 2, 7), 7) -> 5,
        (Array(-7, 1, 2, 3,10), 6)              -> 1,
        (Array(-7, 1, 1, 2, 3, 10), 0)          -> 1,
        (Array( 3,-1,-4, 0, 1, 3, 5), 0)        -> 2,
      )
      
      val impls = Seq(
        TaskCDynamic.hasSubArrayWithSum _,
        TaskCDynamicScala.hasSubArrayWithSum _,
      )
      
      impls.foreach { impl =>
        data.foreach { case ((a, k), exp) =>
          impl(a, k) shouldEqual exp
        }
      }
      
    }
    
  }
}
