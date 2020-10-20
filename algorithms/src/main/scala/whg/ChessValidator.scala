package whg

import tools.spec.ASpec

object ChessValidator {
  def wrongState(msg: String) = throw new IllegalArgumentException(msg)
  
  sealed trait Color
  final case object White extends Color
  final case object Black extends Color

  sealed abstract class CFigure(val c: Color, private val s: Char) {
    def rep: Char = c match {
      case White => s.toUpper
      case Black => s.toLower
    }
  }
  final case class Pawn(color: Color) extends CFigure(color, 'p')   // Piece, Пешка
  final case class Queen(color: Color) extends CFigure(color, 'q')
  final case class King(color: Color) extends CFigure(color, 'k')
  final case class Bishop(color: Color) extends CFigure(color, 'b') // Слон
  final case class Knight(color: Color) extends CFigure(color, 'n') // Конь
  final case class Rook(color: Color) extends CFigure(color, 'r')   // Ладья

  case class Loc(x: Int, y: Int) {
    def isOnBoard: Boolean = Loc.isOnBoard(x) && Loc.isOnBoard(y)
  }
  
  object Loc {
    def iToX(i: Int) = i - 'a' + 1
    def iToY(i: Int) = i - '1' + 1
    def isOnBoard(a: Int): Boolean = a >= 1 && a <= 8
    def parse(loc: String) = Option(loc.trim)
      .filter(_.length == 2)
      .map(_.toCharArray)
      .map { case Array(x, y) => (iToX(x), iToY(y)) }
      .filter { case (x, y) => isOnBoard(x) && isOnBoard(y) }
      .map { case (x, y) => Loc(x, y) }
    def parseOrEx(loc: String) = parse(loc)
      .getOrElse(wrongState(s"Wrong location given: `$loc`"))
    def move(l: Loc, dx: Int, dy: Int) = Option(l)
      .map { case Loc(x, y) => Loc(x + dx, y + dy) }
      .filter(_.isOnBoard)
  }

  case class Move(start: Loc, finish: Loc)
  object Move {
    def parse(move: String) = Option(move.trim)
      .filter(_.length == 4)
      .map(_.splitAt(2))
      .flatMap { case (s1, s2) => for {
        x <- Loc.parse(s1)
        y <- Loc.parse(s2)
      } yield Move(x, y) }
    def parseOrEx(move: String) = parse(move)
      .getOrElse(wrongState(s"Wrong move given: `$move"))
  }

  type TCell = Option[CFigure] // cell
  type TRow = Vector[TCell]    // row
  type TBoard = Vector[TRow]   // board

  class Board(private val b: TBoard) {
    import Board.repRow

    /** representation */
    def rep: String = b.map(repRow).reverseIterator.mkString("\n")
    /** basic operations */
    def at(loc: Loc): TCell = b(loc.y - 1)(loc.x - 1)
    def isOccupiedAt(loc: Loc): Boolean = at(loc).isDefined
    def isFreeAt(loc: Loc): Boolean = !isOccupiedAt(loc)
    def isColorAt(loc: Loc, c: Color): Boolean = at(loc).exists(_.c == c)
    def updated(loc: Loc, of: TCell) = new Board(b.updated(loc.y - 1, b(loc.y -1).updated(loc.x - 1, of)))
    /** no any validation! */
    def put(loc: Loc, f: CFigure): Board = updated(loc, Some(f))
    def clear(loc: Loc): Board = updated(loc, None)
    /** there is only one validation, whether source point is empty */
    def move(m: Move): (Board, Boolean) = m match {
      case Move(start, finish) => at(start) match {
        case Some(f) => (clear(start).put(finish, f), true) 
        case None    => (this,                      false)
      }
    }

    /** actually, only for tests and handy representation */
    def at(loc: String): TCell = at(Loc.parseOrEx(loc))
    def isOccupiedAt(loc: String): Boolean = isOccupiedAt(Loc.parseOrEx(loc))
    def isFreeAt(loc: String): Boolean = isFreeAt(Loc.parseOrEx(loc))
    def isColorAt(loc: String, c: Color): Boolean = isColorAt(Loc.parseOrEx(loc), c)
    def put(loc: String, f: CFigure): Board = put(Loc.parseOrEx(loc), f)
    def clear(loc: String): Board = clear(Loc.parseOrEx(loc))
    def move(m: String): (Board, Boolean) = move(Move.parseOrEx(m))
  }

  object Board {
    val EMPTY = '.'
    def repCell(f: TCell) = f.map(_.rep).getOrElse(EMPTY)
    def repRow(row: TRow) = row.map(repCell).mkString

    def fill8 = Vector.fill[TCell](8) _
    def emptyRow = fill8(None)
    def pawns(c: Color) = fill8(Some(Pawn(c)))
    def row(c: Color) = Seq(
      Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook
    ).map(f => Some(f(c))).toVector

    def empty = new Board(
      Vector.fill(8)( // 8 rows    1..8
        emptyRow     // 8 columns a..h
      )
    )
    def initial = new Board(
      Vector(
        row(White),
        pawns(White),
        emptyRow, emptyRow, emptyRow, emptyRow,
        pawns(Black),
        row(Black)
      ))
  }
  
  object PossibleMove {
    val oneDir = Seq(-1, 0, 1)
    val oneDeltas = for {
      x <- oneDir
      y <- oneDir
      if !(x == 0 && y == 0)
    } yield (x, y)
    val kMove = Seq(-2, -1, 1, 2)
    val kDeltas = for {
      x <- kMove
      y <- kMove
      if math.abs(x) != math.abs(y)
    } yield (x, y)
    def applyAndFilter(l: Loc, ds: Seq[(Int, Int)]) = ds.flatMap { case (dx, dy) => Loc.move(l, dx, dy) }
    def oneSteps(l: Loc) = applyAndFilter(l, oneDeltas)  // King
    def knightSteps(l: Loc) = applyAndFilter(l, kDeltas) // Knight
    def seven(l: Loc, f: (Int, Loc) => Loc) = (1 to 7).map(d => f(d, l)).filter(_.isOnBoard)
    def toR(l: Loc)  = seven(l, (d, l) => Loc(l.x + d, l.y)) 
    def toL(l: Loc)  = seven(l, (d, l) => Loc(l.x - d, l.y))    
    def toU(l: Loc)  = seven(l, (d, l) => Loc(l.x, l.y + d))    
    def toD(l: Loc)  = seven(l, (d, l) => Loc(l.x, l.y - d))    
    def toRU(l: Loc) = seven(l, (d, l) => Loc(l.x + d, l.y + d))
    def toLU(l: Loc) = seven(l, (d, l) => Loc(l.x - d, l.y + d))
    def toRD(l: Loc) = seven(l, (d, l) => Loc(l.x + d, l.y - d))
    def toLD(l: Loc) = seven(l, (d, l) => Loc(l.x - d, l.y - d))
    def --(l: Loc) = toLU(l) ++ toRU(l)
    def |(l: Loc) = toU(l) ++ toD(l)
    def /(l: Loc) = toRU(l) ++ toLD(l)
    def \(l: Loc) = toLU(l) ++ toRD(l)
    def +(l: Loc) = --(l) ++ |(l)       // Rook
    def X(l: Loc) = /(l) ++ \(l)        // Bishop
    def Ж(l: Loc) = this.+(l) ++ X(l)   // Queen
  }
  /**
    * TODO:
    * 
    * - possible move for each figure
    *   - general
    *   - filtered by occupied cells and cells on board
    * - "check" ("шах")
    * - mirroring for opposite side for Pawn
    * 
    */
  
}

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