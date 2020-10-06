package hackerrank.d200917

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SherlockAndAnagramsSpec extends AnyFunSpec with Matchers {
  describe("all subs") {
    import SherlockAndAnagramsPlain._
    
    it("a") {
      val s = "ab"
      val EXP = Seq("a", "b")
      
      val r: Seq[(Int, Int, Int)] = allCombinations(s.length)
      println(r)
    }
    it("b1") {
      val s = "ab"
      val EXP = Seq(("a", "b"))
      val res = allSubs(s, allCombinations(s.length))
      res.toList should contain theSameElementsInOrderAs EXP.toList
    }
    it("b2") {
      val s = "abc"
      val EXP = Seq(
        ("a", "b"),
        ("a", "c"),
        ("b", "c"),
        ("ab","bc"),
      )
      val res = allSubs(s, allCombinations(s.length))
      res.toList should contain theSameElementsAs EXP.toList
    }
  }

}
