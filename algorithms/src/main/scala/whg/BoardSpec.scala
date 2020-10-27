package whg

import tools.spec.ASpec

class BoardSpec extends ASpec {
  import Board.{toLoc, toMove} // implicit conversion String => Move and String => Location, BE CAREFUL!
  
  describe("board operations") {

    it("at") {
      val b = Board.initial

      val figures = for {
        (c, y) <- Seq((White, 1), (Black, 8))
        (f, x) <- Board.firstRow(c) zip LazyList.from('a').map(_.toChar)
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
      val b2 = b.move("e2e4")
      b2.isRight shouldEqual true
      b2 match {
        case Right(b2) =>
          b2.at("e2") shouldEqual None
          b2.at("e4") shouldEqual Some(Pawn(White))
        case Left(_) => ??? 
      }
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
    
    it("findKing") {
      val b = Board.initial
      val data = Seq(
        White -> "e1",
        Black -> "e8"
      ).map { case (in, out) => in -> Loc(out) }
      
      for {
        (in, out) <- data
      } b.findKing(in) shouldEqual out
      
      for {
        (in, out) <- data
      } Board.findKing(b, in) shouldEqual Some(out) 
    }
  }

}
