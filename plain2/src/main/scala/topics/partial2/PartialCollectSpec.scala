package topics.partial2

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PartialCollectSpec extends AnyFunSpec with Matchers {
  describe("should extract pairs") {
    import PartialCollect._
    
    it("nothing match: 1") {
      val test1 = "abcde"
      stringToMap(test1) shouldEqual Map.empty[String, String]
    }
    
    it("nothing match: 2") {
      val test1 = "a b, c d, e"
      stringToMap(test1) shouldEqual Map.empty[String, String]
    }
    
    it("pairs: 1") {
      stringToMap("a=1 b=2 c=3 d e f") shouldEqual Map("a"->"1", "b"->"2", "c"->"3")
    }

    it("pairs: 1T") {
      stringToMapSI("a=1 b=2 c=3 d e f") shouldEqual Map("a"->1, "b"->2, "c"->3)
    }
    
  }

}
