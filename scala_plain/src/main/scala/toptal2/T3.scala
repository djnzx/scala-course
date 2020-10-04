package toptal2

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object T3 {

  def bToI(b: Boolean) = if (b) 1 else 0

  def hasSubArrayWithSum(xs: Array[Int], k: Int): Int = {
    xs.foldLeft((Set.empty[Int], 0, 0)) { case ((parts, sum, cnt), item) =>
      val sum2 = sum + item
      val delta1 = sum2 == k
      val delta2 = parts.contains(sum2 - k)
      (parts + sum2, sum2, cnt + bToI(delta1) + bToI(delta2))
    }._3
  }


  def solution(a: Array[Int], s: Int): Int = {
    ???
  }

}

class T3Spec extends AnyFunSpec with Matchers {
  import T3._
  it("3") {
    1 shouldEqual 1
  }
  
}