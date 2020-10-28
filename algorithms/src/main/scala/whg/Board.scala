package whg

import whg.Board.{TBoard, TCell}
import ExceptionSyntax._

class Board(private val a: TBoard) {
  /** implementation dependent operations */
  private def at(x: Int, y: Int): TCell = a(y - 1)(x - 1)
  private def upd(loc: Loc, of: TCell) = new Board(a.updated(loc.y - 1, a(loc.y -1).updated(loc.x - 1, of)))
  /** basic low level operations, made public only for tests */
  def put(loc: Loc, f: CFigure) = upd(loc, Some(f))
  def clear(loc: Loc) = upd(loc, None)
  /** public API */
  def at(loc: Loc): TCell = at(loc.x, loc.y)
  def isFreeAt(loc: Loc) = at(loc).isEmpty
  def isColorAt(loc: Loc, c: Color) = at(loc).exists(_.c == c)
  def isWhiteAt(loc: Loc) = isColorAt(loc, White)
  def isBlackAt(loc: Loc) = isColorAt(loc, Black)
  def move(m: Move) = 
    at(m.start)
      .map(f => clear(m.start).put(m.finish, f))
      .toRight(ImStartCellIsEmpty(m))

  /**
    * orDie semantic is used only in tests
    */
  def moveOneOrDie(m: Move) = move(m).fold(_.die, identity)
  def moveAllOrDie(ms: Seq[Move]) = ms.foldLeft(this)((b, m) => b.moveOneOrDie(m))  
  def findKingOrDie(c: Color) = Board.findKing(this, c).getOrElse(!msg.noKing(c))
  override def toString: String = Board.repBoard(a)
}

object Board {
  type TCell = Option[CFigure]
  type TRow = Vector[TCell]
  type TBoard = Vector[TRow]

  val EMPTY = '.'
  /** cell representation */
  def repCell(f: TCell) = f.map(_.rep).getOrElse(EMPTY)
  /** row representation */
  def repRow(row: TRow) = row.map(repCell).mkString
  /** board representation */
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
