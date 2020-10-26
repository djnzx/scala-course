package whg

import tools.spec.ASpec

class CheckSpec extends ASpec {
  import Check._
  val b = Board.initial
  
  it("1") {
    isUnderTheCheck(b, White)
  }
  
  it("2") {
    import Directions._
    
    // TODO: doesn't work as expected, maybe some tests need to be refactored
    println(
      mvPawnBite(b.findKing(White), b)
    )
  }

}
