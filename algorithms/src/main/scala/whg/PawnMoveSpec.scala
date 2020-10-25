package whg

import tools.spec.ASpec

class PawnMoveSpec extends ASpec {
  import Directions._
  
  describe("pawn moves") {
    val b = Board.initial
    
    it("fwd1") {
      val t = Seq(
        Loc(1,2) -> Loc(1,3),
        Loc(2,2) -> Loc(2,3),
        Loc(3,2) -> Loc(3,3),
        Loc(4,2) -> Loc(4,3),
        Loc(1,7) -> Loc(1,6),
        Loc(2,7) -> Loc(2,6),
        Loc(3,7) -> Loc(3,6),
        Loc(4,7) -> Loc(4,6),
      ).map { case (in, out) => in -> Some(out)}
      
      val f: Seq[(Loc, None.type)] = Seq(
        Loc(1,1),
        Loc(2,1),
        Loc(3,1),
        Loc(4,1),
        Loc(5,8),
        Loc(6,8),
        Loc(7,8),
        Loc(8,8),
        
        Loc(3,3),
        Loc(4,4),
      ).map(_ -> None)
      
      for {
        (in, out) <- t ++ f
      } mvPawn1(in, b) shouldEqual out
    }
    
    it("fwd2") {
      val t = Seq(
        Loc(1,2) -> Loc(1,4),
        Loc(2,2) -> Loc(2,4),
        Loc(3,2) -> Loc(3,4),
        Loc(4,2) -> Loc(4,4),
        Loc(1,7) -> Loc(1,5),
        Loc(2,7) -> Loc(2,5),
        Loc(3,7) -> Loc(3,5),
        Loc(4,7) -> Loc(4,5),
      ).map { case (in, out) => in -> Some(out)}
      
      val f = Seq(
        Loc(1,1),
        Loc(2,1),
        Loc(3,1),
        Loc(4,1),
        Loc(5,8),
        Loc(6,8),
        Loc(7,8),
        Loc(8,8),
        
        Loc(3,3),
        Loc(4,4),
      ).map(_ -> None)
      
      for {
        (in, out) <- t ++ f
      } mvPawn2(in, b) shouldEqual out
    }
    
    it("fwd2 #2") {
      val t = Seq( // pawn in orig. position moved on 2 steps
        Loc(5,2) -> Loc(5,4),
        Loc(6,2) -> Loc(6,4),
        Loc(7,2) -> Loc(7,4),
        Loc(8,2) -> Loc(8,4),
        Loc(1,7) -> Loc(1,5),
        Loc(2,7) -> Loc(2,5),
        Loc(3,7) -> Loc(3,5),
        Loc(4,7) -> Loc(4,5),
      ).map { case (in, out) => in -> Some(out)}

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
        // non pawns
        Loc(1,1),
        Loc(2,1),
        Loc(3,1),
        Loc(4,1),
        Loc(5,8),
        Loc(6,8),
        Loc(7,8),
        Loc(8,8),
      ).map(_ -> None)

      val b5 = b.move(Seq(
        "a2a3",
        "b2b3",
        "c2c3",
        "d2d3",
      ))

      for {
        (in, out) <- t ++ f1 ++ f2
      } mvPawn2(in, b5) shouldEqual out

    }
    
    it("fwd 1 + 2") {
      // first row moved 1 step
      val b7 = b.move(Seq(
        "a2a3",
        "b2b3",
        "c2c3",
        "d2d3",
        "a3a4",
        "b3b4",
      ))

      println(b7.rep)
      
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
      } mvPawnFwd(in, b7) shouldEqual out 
    }
    
    it("bite") {
      val data1 = Seq(
        Loc(1,2) -> Seq(Loc(2,3)),           // white. corner
        Loc(2,2) -> Seq(Loc(1,3), Loc(3,3)), // white. non-corner
        Loc(8,7) -> Seq(Loc(7,6)),           // black. corner
        Loc(7,7) -> Seq(Loc(6,6), Loc(8,6)), // black. non-corner
      ).map { case (in, out) => in -> out.map(Seq(_)) }
      val data2 = Seq(
        Loc(1,1), // non pawn
        Loc(3,3), // empty
      ).map(_->Seq())
      for {
        (in, out) <- data1 ++ data2
      } mvPawnBite(in, b) shouldEqual out

    }
    
    it("fwd 1 + 2 + bite L + R") {
      println(mvPawn(Loc(5,2), b))
    }
  }

}
