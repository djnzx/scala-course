package whg

import tools.spec.ASpec

class ChessValidatorSpec extends ASpec {
  import ChessValidator._
  import ChessValidator.Board._

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
        Board.pawns(White) -> "P" * 8,
        Board.pawns(Black) -> "p" * 8,
      )
      for {
        (pp, r) <- data
      } repRow(pp) shouldEqual r
    }

    it("start row") {
      val data = Seq(
        row(White) -> "RNBQKBNR",
        row(Black) -> "rnbqkbnr"
      )
      for {
        (rw, r) <- data
      } repRow(rw) shouldEqual r
    }

    it("empty board") {
      Board.empty.rep shouldEqual
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
      Board.initial.rep shouldEqual
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

  describe("input data validation") {

    it("location") {
      val good = Seq(
        " e2"  -> Loc(5, 2),
        "e4 "  -> Loc(5, 4),
        " a1 " -> Loc(1, 1),
        "a2"   -> Loc(1, 2),
        "b3"   -> Loc(2, 3),
        "h8"   -> Loc(8, 8),
      ).map { case (in, out) => in -> Some(out) }
      val bad = Seq(
        "a",
        "abc",
        "a0", "h0", "i0",
        "x1", "+1", "i1", "k1",
        "i9"
      ).map(_ -> Option.empty[Loc])

      for {
        (in, out) <- good ++ bad
      } Loc.parse(in) shouldEqual out
    }

    it("move") {
      val good = Seq(
        "e2e4" -> Move(Loc(5, 2), Loc(5, 4))
      ).map { case (in, out) => in -> Some(out) }
      val bad = Seq(
        "adfvd",
        "ab3",
        "a0",
        "e0e4",
        "c1c9"
      ).map(_ -> Option.empty[Move])

      for {
        (in, out) <- good ++ bad
      } Move.parse(in) shouldEqual out
    }

  }

  describe("board") {

    it("at") {
      val b = Board.initial

      val figures = for {
        (c, y) <- Seq((White, 1), (Black, 8))
        (f, x) <- Board.row(c) zip LazyList.from('a').map(_.toChar)
      } yield s"$x$y" -> f

      val pawns = for {
        (c, y) <- Seq((White, 2), (Black, 7))
        x <- 'a' to 'h'
      } yield s"$x$y" -> Pawn(c)

      val occupied = figures ++ pawns.map { case (in, out) => in -> Some(out) }

      val empty = for {
        x <- 'a' to 'h'
        y <- 3 to 6
      } yield s"$x$y" -> None

      for {
        (in, out) <- occupied ++ empty
      } b.at(in) shouldEqual out
    }

    it("clear") {
      val b = Board.initial
      val loc = "a1"
      b.at(loc) shouldEqual Some(Rook(White))

      val b2 = b.clear(loc)
      b2.at(loc) shouldEqual None
    }

    it("put") {
      val b = Board.initial
      b.clear("e2") match {
        case b2 =>
          val l = "e4"
          val f = Pawn(White)
          b2.put(l, f) match {
            case b3 =>b3.at(l) shouldEqual Some(f)
          }
      }
    }

    it("move") {
      val b = Board.initial
      b.move("e2e4") match {
        case (b2, valid) =>
          valid shouldEqual true
          b2.at("e2") shouldEqual None
          b2.at("e4") shouldEqual Some(Pawn(White))
      }
    }

    it("isOccupiedAt") {
      val b = Board.initial
      val data = Seq(
        "e2" -> true,
        "e3" -> false,
      )
      for {
        (in, out) <- data
      } b.isOccupiedAt(in) shouldEqual out
    }

    it("isFreeAt") {
      val b = Board.initial
      val data = Seq(
        "e2" -> false,
        "e3" -> true,
      )
      for {
        (in, out) <- data
      } b.isFreeAt(in) shouldEqual out
    }

    it("isColorAt") {
      val b = Board.initial
      val data = Seq(
        ("e2", White) -> true,
        ("e2", Black) -> false,
        ("e3", White) -> false,
        ("e3", Black) -> false,
      )
      for {
        ((cell, c), r) <- data
      } b.isColorAt(cell, c) shouldEqual r
    }
  }

  describe("possible moves") {
    import PossibleMove._

    it("r,l,u,d") {
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

    it("1") {
      println(kDeltas)
    }
  }

}
