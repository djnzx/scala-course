package whg

import Directions._
import whg.Board.TCell

object Check {

  def isWhiteUnderTheCheck(b: Board) = isUnderTheCheck(b, White)
  def isBlackUnderTheCheck(b: Board) = isUnderTheCheck(b, Black)
  def threats(c: Color) = {
    val another = c.another
    (Queen(another), Rook(another), Bishop(another), Knight(another), Pawn(another))
  }
  def isUnderTheCheck(bd: Board, c: Color): Boolean = {
    val (q, r, b, n, p) = threats(c)
    def detectThreatHV(of: TCell) = of.contains(q) || of.contains(r) // vert & hor
    def detectThreatD (of: TCell) = of.contains(q) || of.contains(b) // diag
    def detectThreatKN(of: TCell) = of.contains(n)                   // knight
    def detectThreatPW(of: TCell) = of.contains(p)                   // pawn
//    val firstOccupied   = (ll: Seq[Loc]) => ll.dropWhile(b.at(_).isEmpty).headOption.flatMap(b.at)
    val firstOccupied   = (ll: Seq[Loc]) => ll.find(bd.at(_).isDefined).flatMap(bd.at)
    // find the king on the board
    val kingAt = bd.findKing(c)
    // get the dirs
    val cross  = mvRook(kingAt)
    val diag   = mvBishop(kingAt)
    val knight = mvKnight(kingAt)
    val pawn   = mvPawnBite(kingAt, bd)
    val threatAtHV = cross .filter(dir => detectThreatHV(firstOccupied(dir))).flatten.nonEmpty
    val threatAtD  = diag  .filter(dir => detectThreatD (firstOccupied(dir))).flatten.nonEmpty
    val threatAtKN = knight.filter(dir => detectThreatKN(firstOccupied(dir))).flatten.nonEmpty
    val threatAtPW = pawn  .filter(dir => detectThreatPW(firstOccupied(dir))).flatten.nonEmpty
    threatAtHV || threatAtD || threatAtKN || threatAtPW
  }
  
}
