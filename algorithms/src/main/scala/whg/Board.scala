package whg

import whg.Board.{TBoard, TCell}
import Tools.wrongState

class Board(private val b: TBoard) {
  def rep = Board.repBoard(b)

  /** basic operations */
  def at(x: Int, y: Int): TCell = b(y - 1)(x - 1)
  def at(loc: Loc): TCell = at(loc.x, loc.y)
  def isFreeAt(loc: Loc) = at(loc).isEmpty
  def isColorAt(loc: Loc, c: Color) = at(loc).exists(_.c == c)
  def isWhiteAt(loc: Loc) = isColorAt(loc, White)
  def isBlackAt(loc: Loc) = isColorAt(loc, Black)
  def updated(loc: Loc, of: TCell) = new Board(b.updated(loc.y - 1, b(loc.y -1).updated(loc.x - 1, of)))
  def put(loc: Loc, f: CFigure) = updated(loc, Some(f))
  def clear(loc: Loc) = updated(loc, None)
  /** one move => Either */
  def move(m: Move) = m match {
    case Move(start, finish) => at(start) match {
      case Some(f) => Right(clear(start).put(finish, f))
      case None    => Left(s"source cell `$start`is empty")
    }
  }
  /** one move => Board */
  def moveOrEx(m: Move) = move(m).fold(err => wrongState(err), identity)
  /** many moves => Board */
  def moveAll(ms: Seq[Move]): Board = ms match {
    case Nil     => this
    case m::tail => moveOrEx(m).moveAll(tail)
  }

  def findKing(c: Color) = Board.findKing(this, c).getOrElse(wrongState(s"there is no king of color $c"))
}

object Board {
  implicit def toLoc(s: String) = Loc.parseOrEx(s)
  implicit def toMove(s: String) = Move.parseOrEx(s)
  
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
