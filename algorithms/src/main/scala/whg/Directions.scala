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
  
  def mvKing(l: Loc) = moveAndFilter(l, oneDeltas).map(Seq(_))
  def mvRook(l: Loc) = Seq(toL(l),  toR(l),  toU(l),  toD(l))
  def mvBishop(l: Loc) = Seq(toLU(l), toLD(l), toRU(l), toRD(l))
  def mvQueen(l: Loc) = mvRook(l) ++ mvBishop(l)
  def mvKnight(l: Loc) = moveAndFilter(l, knDeltas).map(Seq(_))
  
  // one step forward
  def mvPawn1(l: Loc, b: Board) = Loc.move(l, 0, vector(l, b)) 
  // two steps forward
  def mvPawn2(l: Loc, b: Board) = (l, b) match {
    case PawnCan2(v) => Loc.move(l, 0, v)
    case _ => None
  }
  // 1 + 2 steps forward
  def mvPawnForward(l: Loc, b: Board) = Seq(mvPawn1(l, b), mvPawn2(l, b)).flatten.map(Seq(_))

  def mvPawnBiteL(l: Loc, b: Board) = Loc.move(l, -1, vector(l, b)) 
  def mvPawnBiteR(l: Loc, b: Board) = Loc.move(l,  1, vector(l, b))
  def mvPawnBite(l: Loc, b: Board) = Seq(mvPawnBiteL(l, b), mvPawnBiteR(l, b)).flatten.map(Seq(_))

  /**
    * get the color of pawn for vector
    * one forward
    * + (opt) forward
    * + (diagonal) bite left + bite right
    * + white/black different directions
    */
  def mvPawn(l: Loc, b: Board) = mvPawnForward(l, b) ++ mvPawnBite(l, b)
  
  object PawnCan2 {
    def unapply(arg: (Loc, Board)): Option[Int] = arg match {
      case (l, b) if b.isWhiteAt(l) && l.y == 2 => Some(2)
      case (l, b) if b.isBlackAt(l) && l.y == 7 => Some(-2)
      case _ => None
    }
  }
  
  def vector(l: Loc, b: Board) = b.at(l).get.c match {
    case White =>  1
    case Black => -1
  }

}
