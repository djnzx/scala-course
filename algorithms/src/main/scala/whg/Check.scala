package whg

import whg.Directions._

object Check {

  def figures(c: Color) = {
    val another = c.another
    (Queen(another), Rook(another), Bishop(another), Knight(another), Pawn(another))
  }
  def isUnderTheCheck(b: Board, c: Color): Boolean = {
    val (fq, fr, fb, fn, fp) = figures(c)
    def firstOccupiedIs(ll: Seq[Loc], p: CFigure => Boolean) = ll.flatMap(b.at).exists(p)
    val kingAt = b.findKing(c)
    val threats: Seq[(Seq[Seq[Loc]], CFigure => Boolean)] = Seq(
      mvRook(kingAt)         -> (f => f == fq || f == fr),
      mvBishop(kingAt)       -> (f => f == fq || f == fb),
      mvKnight(kingAt)       -> (f => f == fn),
      mvPawnBite(kingAt, b)  -> (f => f == fp),
    )
    threats.foldLeft(false) { case (a, (ll, p)) =>
      a || ll.exists(firstOccupiedIs(_, p))
    }
  }
  
  def foldCheck(check: Option[Color], f: Color => Boolean) = check.fold(true)(f)
  
}
