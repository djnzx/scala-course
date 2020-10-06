package hackerrank.d200911

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class CommonChildSpec extends AnyFunSpec with Matchers {
  describe("common child") {
    import  CommonChild._
    
    it("a") {
      Map(
        (("HARRY", "SALLY"), 2),
        (("AA", "BB"), 0),
        (("SHINCHAN", "NOHARAAA"), 3),
        (("ABCDEF", "FBDAMN"), 2),
      ).foreach { case ((a, b), exp) => 
        commonChild(a, b) shouldEqual exp
      }
    }
  }
}
