package google

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Task31Levenstein {

  def min3(a: Int, b: Int, c: Int) = a min b min c

  def distance(s1: String, s2: String): Int = {
    def cost(i: Int, j: Int) = if (s1(i-1) == s2(j-1)) 0 else 1
    val l1 = s1.length
    val l2 = s2.length
    if (l1 == 0) l2
    else if (l2 == 0) l1
    else {
      val m = Array.ofDim[Int](l1 + 1, l2 + 1)
      (0 to l1) foreach(i => m(i)(0) = i)
      (0 to l2) foreach(j => m(0)(j) = j)
      (1 to l1) foreach { i =>
        (1 to l2) foreach { j =>
          m(i)(j) = min3(
            m(i-1)(j) + 1,
            m(i)(j-1) + 1,
            m(i-1)(j-1) + cost(i, j)
          )
        }
      }
      m(l1)(l2)
    }
  }
  
}

class Task31LevensteinSpec extends AnyFunSpec with Matchers {
  
  import Task31Levenstein._
  
  describe("distance") {
    it("1") {
      distance("abc","") shouldEqual 3
      distance("","cd") shouldEqual 2
      distance("","i") shouldEqual 1
      distance("help","hello") shouldEqual 2
      distance("string","sting") shouldEqual 1
      distance("string","king") shouldEqual 3
    }
    
  }
  
}