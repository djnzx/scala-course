package whg

import whg.Board.{TBoard, TCell}
import Tools.wrongState

class Board(private val b: TBoard) {
  def rep = Board.repBoard(b)

  /** basic operations */
  def at(x: Int, y: Int): TCell = b(y - 1)(x - 1)
  def at(loc: Loc): TCell = at(loc.x, loc.y)
  def isOccupiedAt(loc: Loc): Boolean = at(loc).isDefined
  def isFreeAt(loc: Loc): Boolean = !isOccupiedAt(loc)
  def isColorAt(loc: Loc, c: Color): Boolean = at(loc).exists(_.c == c)
  def isWhiteAt(loc: Loc): Boolean = at(loc).exists(_.isWhite)
  def isBlackAt(loc: Loc): Boolean = at(loc).exists(_.isBlack)
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
  def findKing(c: Color) = Board.findKing(this, c).getOrElse(wrongState(s"there is no king of color $c"))
  
  /** actually, only for tests and handy representation */
  def at(loc: String): TCell = at(Loc.parseOrEx(loc))
  def isOccupiedAt(loc: String): Boolean = isOccupiedAt(Loc.parseOrEx(loc))
  def isFreeAt(loc: String): Boolean = isFreeAt(Loc.parseOrEx(loc))
  def isColorAt(loc: String, c: Color): Boolean = isColorAt(Loc.parseOrEx(loc), c)
  def put(loc: String, f: CFigure): Board = put(Loc.parseOrEx(loc), f)
  def clear(loc: String): Board = clear(Loc.parseOrEx(loc))
  def move(m: String): (Board, Boolean) = move(Move.parseOrEx(m))
  def move(ms: Seq[String]): Board = ms match {
    case Nil     => this
    case m::tail => move(m)._1.move(tail)
  }
}

object Board {
  /** types */
  type TCell = Option[CFigure]
  type TRow = Vector[TCell]
  type TBoard = Vector[TRow]

  val EMPTY = '.'
  /** cell representation */
  def repCell(f: TCell) = f.map(_.rep).getOrElse(EMPTY)
  /** row representation */
  def repRow(row: TRow) = row.map(repCell).mkString
  /** representation */
  def repBoard(b: TBoard) = b.map(repRow).reverseIterator.mkString("\n")

  def fill8[A] = Vector.fill[A](8) _
  def emptyRow = fill8(None)
  def pawnsRow(c: Color) = fill8(Some(Pawn(c)))
  def firstRow(c: Color) = Seq(
    Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook
  ).map(f => Some(f(c))).toVector
  /** empty board */
  def empty = new Board(fill8(emptyRow))
  /** initial board */
  def initial = new Board(
    Vector(
      firstRow(White),
      pawnsRow(White),
      emptyRow, emptyRow, emptyRow, emptyRow,
      pawnsRow(Black),
      firstRow(Black)
    )
  )
  def findKing(b: Board, c: Color) =
    (1 to 8).flatMap { y =>
      (1 to 8)
        .filter(b.at(_,y).contains(King(c)))
        .map(x => Loc(x, y))
        .headOption
    }.headOption

}
