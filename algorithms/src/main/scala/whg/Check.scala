package whg

import whg.Directions._

object Check {

  def isWhiteUnderTheCheck(b: Board) = isUnderTheCheck(b, White)
  def isBlackUnderTheCheck(b: Board) = isUnderTheCheck(b, Black)
  def threats(c: Color) = {
    val another = c.another
    (Queen(another), Rook(another), Bishop(another), Knight(another), Pawn(another))
  }
  def isUnderTheCheck(bd: Board, c: Color): Boolean = {
    val (q, r, b, n, p) = threats(c)
    def firstOccupiedIs(ll: Seq[Loc], p: CFigure => Boolean) = ll.flatMap(bd.at).exists(p)
    // find the king on the board
    val kingAt = bd.findKing(c)
    // get the dirs
    val cross  = mvRook(kingAt)
    val diag   = mvBishop(kingAt)
    val knight = mvKnight(kingAt)
    val pawn   = mvPawnBite(kingAt, bd)
    // check the threats
    val threatHV = cross .exists(firstOccupiedIs(_, f => f == q || f == r))
    val threatD  = diag  .exists(firstOccupiedIs(_, f => f == q || f == b))
    val threatKN = knight.exists(firstOccupiedIs(_, _ == n))
    val threatPW = pawn  .exists(firstOccupiedIs(_, _ == p))
    threatHV || threatD || threatKN || threatPW
//    V2
//    val data: Seq[(Seq[Seq[Loc]], CFigure => Boolean)] = Seq(
//      mvRook(kingAt)         -> (f => f == q || f == r),
//      mvBishop(kingAt)       -> (f => f == q || f == r),
//      mvKnight(kingAt)       -> (_ == n),
//      mvPawnBite(kingAt, bd) -> (_ == p),
//    )
//    data.foldLeft(false) { case (a, (ll, p)) =>
//      a || ll.exists(firstOccupiedIs(_, p))
//    }


  }
  
}
