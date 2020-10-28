package whg.spec

import tools.spec.ASpec
import whg._

class CheckSpec extends ASpec {
  import Check._
  import Implicits._ // implicit conversion String => Move and String => Location, BE CAREFUL!

  val b = Board.initial
  
  it("basic case 1") {
    isKingInCheck(b, White) shouldEqual false
    isKingInCheck(b, Black) shouldEqual false
  }
  
  it("basic case 2") {
    import Directions._
    
    // find the king
    val kingAt = b.findKingOrDie(White)
    
    // rook and queen
    mvRook(kingAt) should contain theSameElementsAs
      List(
        Vector(Loc("d1"), Loc("c1"), Loc("b1"), Loc("a1")),
        Vector(Loc("f1"), Loc("g1"), Loc("h1")),
        Vector(Loc("e2"), Loc("e3"), Loc("e4"), Loc("e5"), Loc("e6"), Loc("e7"), Loc("e8")),
        Vector(),
      )
    
    // bishop and queen
    mvBishop(kingAt) should contain theSameElementsAs
      List(
        Vector(Loc("d2"), Loc("c3"), Loc("b4"), Loc("a5")),
        Vector(),
        Vector(Loc("f2"), Loc("g3"), Loc("h4")),
        Vector(),
      )
    
    // knight
    mvKnight(kingAt) should contain theSameElementsAs
      List(
        Vector(Loc("c2")),
        Vector(Loc("d3")),
        Vector(Loc("f3")),
        Vector(Loc("g2")),
      )
    
    // pawns in bite position
    mvPawnBite(kingAt, b) should contain theSameElementsAs
      Seq(
        Seq(Loc("d2")),
        Seq(Loc("f2")),
      )

  }

  it("check mate #1") {
    val b = Board.initial.moveAllOrDie(Seq(
      "e2e4",
      "e7e5",
      "f1c4",
      "b8c6",
      "d1f3",
      "d7d6",
      "f3f7",
    ))
    Predef.println(b)

    isKingInCheck(b, White) shouldEqual false
    isKingInCheck(b, Black) shouldEqual true
  }

  it("foldCheck") {
    import Check.fold
    fold(None, _ => true) shouldEqual true
    fold(None, _ => false) shouldEqual true
    fold(Some(White), { c => println(s"folding $c"); true}) shouldEqual true
    fold(Some(White), { c => println(s"folding $c"); false}) shouldEqual false
    fold(Some(Black), { c => println(s"folding $c"); true}) shouldEqual true
    fold(Some(Black), { c => println(s"folding $c"); false}) shouldEqual false
  }

}
