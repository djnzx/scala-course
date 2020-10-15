package google.live.t3

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/**
  * https://en.wikipedia.org/wiki/Levenshtein_distance
  * https://medium.com/@kargostack/my-first-interview-code-challenge-levenshtein-distance-d53b38a4d03e
  */
object LevensteinDistance {

  def min3(a: Int, b: Int, c: Int) = a min b min c
  def cost(c1: Char, c2: Char) = if (c1 == c2) 0 else 1
  
  def distance(s1: String, s2: String): Int = {
    val l1 = s1.length
    val l2 = s2.length
    if      (l1 == 0) l2
    else if (l2 == 0) l1
    else {
      val m = Array.ofDim[Int](l1 + 1, l2 + 1)
      s1.indices.foreach { i => m(i)(0) = i }
      s2.indices.foreach { j => m(0)(j) = j }
      s1.indices.foreach { i =>
        s2.indices.foreach { j =>
          m(i+1)(j+1) = min3(
            m(i)(j+1) + 1,
            m(i+1)(j) + 1,
            m(i)(j) + cost(s1(i), s2(j))
          )
        }
      }
      m(l1)(l2)
    }
  }
  
}

class Task31LevensteinSpec extends AnyFunSpec with Matchers {
  import LevensteinDistance._
  
  describe("distance") {
    it("1") {
      distance("abc","") shouldEqual 3
      distance("","cd") shouldEqual 2
      distance("","i") shouldEqual 1
      distance("help","hello") shouldEqual 2
      distance("string","sting") shouldEqual 1
      distance("string","king") shouldEqual 3
      distance("ping","king") shouldEqual 1
    }
    
  }
  
}