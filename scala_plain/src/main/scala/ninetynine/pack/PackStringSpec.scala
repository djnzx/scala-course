package ninetynine.pack

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PackStringSpec extends AnyFunSpec with Matchers {
  
  val dataset = Vector(
    "" -> List(),
    "A" -> List('A'->1),
    "AB" -> List('A'->1, 'B'->1),
    "ABB" -> List('A'->1, 'B'->2),
    "ABBC" -> List('A'->1, 'B'->2, 'C'->1),
    "ABBCDDD" -> List('A'->1, 'B'->2, 'C'->1, 'D'->3),
    "ABBCCCDDE" -> List('A'->1, 'B'->2, 'C'->3, 'D'->2, 'E'->1),
  )

  describe("PackStringClassic") {
    import PackString.ClassicFP._
    
    it("6x") {
      for {
        (input, exp) <- dataset
      } input.pack shouldEqual exp 
    }
  }
  
  
}
