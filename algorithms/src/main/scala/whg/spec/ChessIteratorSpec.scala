package whg.spec

import tools.spec.ASpec
import whg.ChessIterator

class ChessIteratorSpec extends ASpec {
  
  it("iterator from file") {
    val ci: ChessIterator = ChessIterator.resource("chess.txt")
    ci.toVector should contain theSameElementsInOrderAs
      Vector(
        Array(0, 6, 0, 5),
        Array(1, 6, 1, 4),
        Array(2, 6, 2, 5),
        Array(3, 6, 3, 4),
        Array(0, 0, 0, 1),
      )
  }
  
}
