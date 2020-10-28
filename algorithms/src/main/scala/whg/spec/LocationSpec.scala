package whg.spec

import tools.spec.ASpec
import whg._

class LocationSpec extends ASpec {
  
  describe("input data validation") {

    it("location parse") {
      val good = Seq(
        " e2"  -> Loc(5, 2),
        "e4 "  -> Loc(5, 4),
        " a1 " -> Loc(1, 1),
        "a2"   -> Loc(1, 2),
        "b3"   -> Loc(2, 3),
        "h8"   -> Loc(8, 8),
      ).map { case (in, out) => in -> Right(out) }
      val bad = Seq(
        "a",
        "abc",
        "a0", "h0", "i0",
        "x1", "+1", "i1", "k1",
        "i9"
      ).map(x => (x, Left(ImErrorParsingLocation(x))))

      for {
        (in, out) <- good ++ bad
      } Loc.parse(in) shouldEqual out
    }

    it("move parse") {
      val good: Seq[(String, Right[Nothing, Move])] = Seq(
        "e2e4" -> Move(Loc(5, 2), Loc(5, 4)),
      ).map { case (in, out) => in -> Right(out) }
      val bad: Seq[(String, Left[ImErrorParsingMove, Nothing])] = Seq(
        "adfvd",
        "ab3",
        "a0",
        "e0e4",
        "c1c9"
      ).map(x => (x, Left(ImErrorParsingMove(x))))

      for {
        (in, out) <- good ++ bad
      } Move.parse(in) shouldEqual out
    }

    it("move given") {
      val data = Seq(
        Array(0, 6, 0, 5) -> "a2a3",
        Array(1, 6, 1, 4) -> "b2b4",
        Array(2, 6, 2, 5) -> "c2c3",
        Array(3, 6, 3, 4) -> "d2d4",
        Array(0, 0, 0, 1) -> "a8a7",
      )
      for {
        (in, out) <- data
      } Move.fromArray(in) shouldEqual out
    }

    it("move given - full cycle") {
      val readed: Vector[Array[Int]] = ChessIterator.resource("chess.txt").toVector
      val mapped: Vector[String] = readed.map(Move.fromArray)
      val expected = Vector(
        "a2a3",
        "b2b4",
        "c2c3",
        "d2d4",
        "a8a7",
      )
      mapped shouldEqual expected
    }
    
    it("exploring the syntax") {
      import L._
      e(2)>e(4) shouldEqual Move("e2e4")
    }

  }

}
