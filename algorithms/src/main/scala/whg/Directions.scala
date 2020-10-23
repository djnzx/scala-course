package whg

object Directions {
  val oneVals = Seq(-1, 0, 1)
  val oneDeltas = for {
    x <- oneVals
    y <- oneVals
    if !(x == 0 && y == 0)
  } yield (x, y)
  val knVals = Seq(-2, -1, 1, 2)
  val knDeltas = for {
    x <- knVals
    y <- knVals
    if math.abs(x) != math.abs(y)
  } yield (x, y)
  def moveAndFilter(l: Loc, deltas: Seq[(Int, Int)]): Seq[Loc] =
    deltas.flatMap { case (dx, dy) => Loc.move(l, dx, dy) }
  def applyAndFilter(l: Loc, f: (Loc, Int) => Option[Loc]): Seq[Loc] =
    (1 to 7).flatMap(d => f(l, d))
  def toR(l: Loc)  = applyAndFilter(l, (l, d) => Loc.move(l,  d, 0))
  def toL(l: Loc)  = applyAndFilter(l, (l, d) => Loc.move(l, -d, 0))
  def toU(l: Loc)  = applyAndFilter(l, (l, d) => Loc.move(l, 0,  d))
  def toD(l: Loc)  = applyAndFilter(l, (l, d) => Loc.move(l, 0, -d))
  def toRU(l: Loc) = applyAndFilter(l, (l, d) => Loc.move(l,  d,  d))
  def toLU(l: Loc) = applyAndFilter(l, (l, d) => Loc.move(l, -d,  d))
  def toRD(l: Loc) = applyAndFilter(l, (l, d) => Loc.move(l,  d, -d))
  def toLD(l: Loc) = applyAndFilter(l, (l, d) => Loc.move(l, -d, -d))
  
  def mvKing(l: Loc) = moveAndFilter(l, oneDeltas).map(l => Seq(l))
  def mvRook(l: Loc) = Seq(toL(l),  toR(l),  toU(l),  toD(l))
  def mvBishop(l: Loc) = Seq(toLU(l), toLD(l), toRU(l), toRD(l))
  def mvQueen(l: Loc) = mvRook(l) ++ mvBishop(l)
  def mvKnight(l: Loc) = moveAndFilter(l, knDeltas).map(l => Seq(l))
  def mvPawn(l: Loc, b: Board): Seq[Seq[Loc]] = {
    /**
      * get the color of pawn for vector
      * one forward
      * + (opt) forward
      * + (diagonal) bite left + bite right
      * + white/black different directions
      */
    ???
  }
}
