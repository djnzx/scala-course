package whg.spec

import tools.spec.ASpec
import whg._

class PawnMoveSpec extends ASpec {
  import Directions._
  import Implicits._ // implicit conversion String => Move and String => Location, BE CAREFUL!

  describe("pawn moves") {
    val b = Board.initial
    
    it("should move a pawn forward 1 step") {
      val t = Seq(
        L.a(2) -> L.a(3),
        L.b(2) -> L.b(3),
        L.c(2) -> L.c(3),
        L.d(2) -> L.d(3),
        L.e(2) -> L.e(3),
        L.f(2) -> L.f(3),
        L.g(2) -> L.g(3),

        L.c(7) -> L.c(6),
        L.d(7) -> L.d(6),
      ).map { case (in, out) => in -> Some(out)}
      
      // no way to move. because these cell are empty
      val f = Seq(
        Loc(3,3),
        Loc(4,4),
      ).map(_ -> None)
      
      for {
        (in, out) <- t ++ f
      } mvPawn1(in, b) shouldEqual out
    }
    
    it("should move a pawn forward 2 steps (true cases)") {
      // we can do BIG step only from initial position
      val t = Seq(
        L.a(2) -> L.a(4),
        L.b(2) -> L.b(4),
        L.c(2) -> L.c(4),
        L.d(2) -> L.d(4),
        L.e(2) -> L.e(4),
        L.f(2) -> L.f(4),
        L.g(2) -> L.g(4),
        L.h(2) -> L.h(4),
        
        L.a(7) -> L.a(5),
        L.b(7) -> L.b(5),
        L.c(7) -> L.c(5),
        L.d(7) -> L.d(5),
        L.e(7) -> L.e(5),
        L.f(7) -> L.f(5),
        L.g(7) -> L.g(5),
        L.h(7) -> L.h(5),
      ).map { case (in, out) => in -> Some(out)}
      
      for {
        (in, out) <- t
      } mvPawn2(in, b) shouldEqual out
    }
    
    it("should move a pawn forward 2 steps (false cases)") {
      val f1 = Seq(
        // pawns already moved ONE step
        Loc(1,3),
        Loc(2,3),
        Loc(3,3),
        Loc(4,3),
        // empty cells
        Loc(1,6),
        Loc(2,6),
        Loc(3,6),
        Loc(4,6),
        Loc(5,6),
        Loc(6,6),
      ).map(_ -> None)

      val f2 = Seq(
        // wrong positions, non pawns, but actually they don't fit into line 2 or 7
        L.a(1),
        L.b(1),
        L.c(1),
        L.d(1),
        L.c(8),
        L.d(8),
        L.e(8),
        L.f(8),
        // empty positions
        L.c(3),
        L.d(4),
      ).map(_ -> None)

      val board2 = b.moveAllOrDie(Seq(
        "a2a3",
        "b2b3",
        "c2c3",
        "d2d3",
      ))

      for {
        (in, out) <- f1 ++ f2
      } mvPawn2(in, board2) shouldEqual out

    }
    
    it("fwd 1 + 2") {
      // first row moved 1 step
      val board2 = b.moveAllOrDie(Seq(
        "a2a3",
        "b2b3",
        "c2c3",
        "d2d3",
        "a3a4",
        "b3b4",
      ))

//      println(b7.rep)
      
      val data1 = Seq(
        Loc(6,2) -> Seq(Loc(6,3), Loc(6,4)),
        Loc(5,2) -> Seq(Loc(5,3), Loc(5,4)),
        
        Loc(4,3) -> Seq(Loc(4,4)),
        Loc(3,3) -> Seq(Loc(3,4)),
        
        Loc(2,4) -> Seq(Loc(2,5)),
        Loc(1,4) -> Seq(Loc(1,5)),
      ).map { case (in, out) => in -> Seq(out) }
      
      for {
        (in, out) <- data1
      } mvPawnFwd(in, board2) shouldEqual out 
    }
    
    it("bite") {
      val data1 = Seq(
        Loc("a2") -> Seq(Loc("b3")),            // white. corner
        Loc("b2") -> Seq(Loc("a3"), Loc("c3")), // white. non-corner
        Loc("h7") -> Seq(Loc("g6")),            // black. corner
        Loc("g7") -> Seq(Loc("f6"), Loc("h6")), // black. non-corner
      ).map { case (in, out) => in -> out.map(Seq(_)) }
      val data2 = Seq(
        Loc(3,3), // empty
      ).map(_->Seq())
      for {
        (in, out) <- data1 ++ data2
      } mvPawnBite(in, b) shouldEqual out

    }
    
    it("fwd 1 + 2 + bite L + R") {
      mvPawn(Loc("e2"), b) should contain theSameElementsAs
        List(
          List(Loc("e3"), Loc("e4")), 
          List(Loc("d3")), 
          List(Loc("f3"))
        )
    }
  }

}
