package whg.spec

import tools.spec.ASpec
import whg.Board.{EMPTY, emptyRow, firstRow, repRow}
import whg._

class RepresentationSpec extends ASpec {
  
  describe("representation") {
    it("one figure") {
      val data = Seq(
        Pawn(White) -> 'P',
        Pawn(Black) -> 'p',
        Queen(White) -> 'Q',
        Queen(Black) -> 'q',
        King(White) -> 'K',
        King(Black) -> 'k',
        Bishop(White) -> 'B',
        Bishop(Black) -> 'b',
        Knight(White) -> 'N',
        Knight(Black) -> 'n',
        Rook(White) -> 'R',
        Rook(Black) -> 'r',
      )
      for {
        (f, r) <- data
      } f.rep shouldEqual r
    }

    it("empty row") {
      repRow(emptyRow) shouldEqual EMPTY.toString * 8
    }

    it("pawns row") {
      val data = Seq(
        Board.pawnsRow(White) -> "P" * 8,
        Board.pawnsRow(Black) -> "p" * 8,
      )
      for {
        (pp, r) <- data
      } repRow(pp) shouldEqual r
    }

    it("start row") {
      val data = Seq(
        firstRow(White) -> "RNBQKBNR",
        firstRow(Black) -> "rnbqkbnr"
      )
      for {
        (rw, r) <- data
      } repRow(rw) shouldEqual r
    }

    it("empty board") {
      Board.empty.toString shouldEqual
        """........
          |........
          |........
          |........
          |........
          |........
          |........
          |........""".stripMargin
    }

    it("initial board") {
      Board.initial.toString shouldEqual
        """rnbqkbnr
          |pppppppp
          |........
          |........
          |........
          |........
          |PPPPPPPP
          |RNBQKBNR""".stripMargin
    }
  }

}
