package whg

object ChessValidator {
  def wrongState(msg: String) = throw new IllegalArgumentException(msg)

  sealed trait Color extends Product
  final case object White extends Color
  final case object Black extends Color
  object Color {
    val another: Color => Color = {
      case White => Black
      case Black => White
    }
  }

  sealed abstract class CFigure(val c: Color, private val s: Char) {
    def rep: Char = c match {
      case White => s.toUpper
      case Black => s.toLower
    }
    // TODO: consider to represent as Set to make contains O(1)
    def nextFrom(l: Loc): Seq[Loc] = this match {
      case Pawn(c)   => ??? 
      case Queen(c)  => ???
      case King(c)   => ???
      case Bishop(c) => ???
      case Knight(c) => ???
      case Rook(c)   => ???
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
    def pawnSteps(l: Loc) = {
      /**
        * one forward
        * +(opt) forward
        * +bite left + bite right
        */
    }
    def seven(l: Loc, f: (Int, Loc) => Loc) = (1 to 7).map(d => f(d, l)).filter(_.isOnBoard)
    def toR(l: Loc)  = seven(l, (d, l) => Loc(l.x + d, l.y))
    def toL(l: Loc)  = seven(l, (d, l) => Loc(l.x - d, l.y))
    def toU(l: Loc)  = seven(l, (d, l) => Loc(l.x, l.y + d))
    def toD(l: Loc)  = seven(l, (d, l) => Loc(l.x, l.y - d))
    def toRU(l: Loc) = seven(l, (d, l) => Loc(l.x + d, l.y + d))
    def toLU(l: Loc) = seven(l, (d, l) => Loc(l.x - d, l.y + d))
    def toRD(l: Loc) = seven(l, (d, l) => Loc(l.x + d, l.y - d))
    def toLD(l: Loc) = seven(l, (d, l) => Loc(l.x - d, l.y - d))
    def -(l: Loc) = toLU(l) ++ toRU(l)
    def |(l: Loc) = toU(l) ++ toD(l)
    def /(l: Loc) = toRU(l) ++ toLD(l)
    def \(l: Loc) = toLU(l) ++ toRD(l)
    def +(l: Loc) = this.-(l) ++ |(l)   // Rook
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
  case class Chess(private val board: Board, nextC: Color) {

    def nextTurn(b: Board) = copy(board = b, nextC = Color.another(nextC))
    
    /**                new state [Error message, Check]
      *                    ||            ||       ||
      *                    v             V        V    */
    def move(m: String): (Chess, Either[String, Boolean]) =
      Move.parse(m)                                                // move is valid syntactically (syntax, on board)
        .filter(m => board.isColorAt(m.start, nextC))                // check color of start
        .filter(m => board.isFreeAt(m.finish) ||                     // finish is empty   
                    board.isColorAt(m.finish, Color.another(nextC))) // finish has another color
        .map(m => (m, board.at(m.start).get))                      // obtain the source figure
        .filter { case (Move(st, fi), f) => f.nextFrom(st).contains(fi) } // finish in the next moves
        .filter { case (m, f) => isPathFree(m, f) }                 // (optionally) path must be empty
        .map { case (m, _) => board.move(m) }                      // do move
        .fold[(Chess, Either[String, Boolean])](
          (this, Left("wrong move"))
        ) {
          case (b, _) => (nextTurn(b), Right(false)) // TODO: false means "CHECK" 
        }

    def isPathFree(m: Move, f: CFigure): Boolean = true // TODO: implement is PathFree
  }

  object Chess {
    def initial = new Chess(Board.initial, White)
  }
}

