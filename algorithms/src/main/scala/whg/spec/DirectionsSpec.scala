package whg.spec

import tools.spec.ASpec
import whg.{Board, Directions, Loc}

class DirectionsSpec extends ASpec {
  
  describe("possible moves") {
    import Directions._
    import Implicits._ // implicit conversion String => Move and String => Location, BE CAREFUL!

    it("r, l, u, d") {
      val x = Loc(3,4)
      toR(x) should contain theSameElementsAs IndexedSeq(Loc(4,4),Loc(5,4),Loc(6,4),Loc(7,4),Loc(8,4))
      toR(Loc(8,4)) should contain theSameElementsAs IndexedSeq()

      toL(x) should contain theSameElementsAs IndexedSeq(Loc(2,4), Loc(1,4))
      toL(Loc(1,4)) should contain theSameElementsAs IndexedSeq()

      toU(x) should contain theSameElementsAs IndexedSeq(Loc(3,5),Loc(3,6),Loc(3,7),Loc(3,8))
      toU(Loc(1,8)) should contain theSameElementsAs IndexedSeq()

      toD(x) should contain theSameElementsAs IndexedSeq(Loc(3,3),Loc(3,2),Loc(3,1))
      toD(Loc(6,1)) should contain theSameElementsAs IndexedSeq()
    }

    it("ru, lu, rd, ld") {
      toRU(Loc(6,1)) shouldEqual IndexedSeq(Loc(7,2), Loc(8,3))
      toRU(Loc(8,3)) shouldEqual IndexedSeq()

      toLU(Loc(6,1)) shouldEqual IndexedSeq(Loc(5,2), Loc(4,3), Loc(3,4), Loc(2,5), Loc(1,6))
      toLU(Loc(1,1)) shouldEqual IndexedSeq()

      toRD(Loc(5,5)) shouldEqual IndexedSeq(Loc(6,4), Loc(7,3), Loc(8,2))
      toRD(Loc(4,1)) shouldEqual IndexedSeq()

      toLD(Loc(7,4)) shouldEqual IndexedSeq(Loc(6,3), Loc(5,2), Loc(4,1))
      toLD(Loc(1,6)) shouldEqual IndexedSeq()
    }

    it("knight deltas") {
      knDeltas should contain theSameElementsAs
        List((-2, -1), (-2, 1), (-1, -2), (-1, 2), (1, -2), (1, 2), (2, -1), (2, 1))
    }

    it("for empty iterable we can provide ANY predicate") {
      Seq[Int]().forall(_ => 2 == 3) shouldEqual true
    }

    it("pawn available moves") {
      val b = Board.initial

      mvPawn(Loc("a2"), b) should contain theSameElementsAs 
        List(List(Loc("a3"), Loc("a4")), List(Loc("b3")))

      mvPawn(Loc("a7"), b) should contain theSameElementsAs
        List(List(Loc("a6"), Loc("a5")), List(Loc("b6")))
      
      // TODO: refactor that
      val b2 = b.moveOneOrDie("a2a3")
      mvPawn(Loc("a3"), b2) should contain theSameElementsAs
        List(List(Loc("a4")), List(Loc("b4")))
    }
  }

}
