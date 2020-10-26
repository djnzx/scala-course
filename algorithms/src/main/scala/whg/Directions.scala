package whg

object Directions {
  lazy val oneVals = Seq(-1, 0, 1)
  lazy val oneDeltas = for {
    x <- oneVals
    y <- oneVals
    if !(x == 0 && y == 0)
  } yield (x, y)
  lazy val knVals = Seq(-2, -1, 1, 2)
  lazy val knDeltas = for {
    x <- knVals
    y <- knVals
    if math.abs(x) != math.abs(y)
  } yield (x, y)
  def moveAndFilter(l: Loc, deltas: Seq[(Int, Int)]) =
    deltas.flatMap { case (dx, dy) => l.move(dx, dy) }
  def applyAndFilter(l: Loc, f: (Loc, Int) => Option[Loc]) =
    (1 to 7).flatMap(d => f(l, d))
  def toR(l: Loc)  = applyAndFilter(l, (l, d) => l.move( d, 0))
  def toL(l: Loc)  = applyAndFilter(l, (l, d) => l.move(-d, 0))
  def toU(l: Loc)  = applyAndFilter(l, (l, d) => l.move(0,  d))
  def toD(l: Loc)  = applyAndFilter(l, (l, d) => l.move(0, -d))
  def toRU(l: Loc) = applyAndFilter(l, (l, d) => l.move( d,  d))
  def toLU(l: Loc) = applyAndFilter(l, (l, d) => l.move(-d,  d))
  def toRD(l: Loc) = applyAndFilter(l, (l, d) => l.move( d, -d))
  def toLD(l: Loc) = applyAndFilter(l, (l, d) => l.move(-d, -d))

  def mvKing(l: Loc) = moveAndFilter(l, oneDeltas).map(Seq(_))
  def mvRook(l: Loc) = Seq(toL(l),  toR(l),  toU(l),  toD(l))
  def mvBishop(l: Loc) = Seq(toLU(l), toLD(l), toRU(l), toRD(l))
  def mvQueen(l: Loc) = mvRook(l) ++ mvBishop(l)
  def mvKnight(l: Loc) = moveAndFilter(l, knDeltas).map(Seq(_))

  /**
    * describes White Pawn,
    * direction extractor for White Pawn
    * @return Some(1) if board(loc) == white pawn
    */
  object PawnWhite {
    def unapply(args: (Loc, Board)) = args match { case (l, b) =>
      b.at(l) match {
        case Some(Pawn(White)) => Some(+1)
//        case Some(f) if f.isWhite => Some(-1)
        case _                 => None
      }
    }
  }

  /**
    * describes Black Pawn,
    * direction extractor for White Pawn
    * @return Some(-1) if board(loc) == black pawn
    */
  object PawnBlack {
    def unapply(args: (Loc, Board)) = args match { case (l, b) =>
      b.at(l) match {
        case Some(Pawn(Black)) => Some(-1)
//        case Some(f) if f.isBlack => Some(-1)
        case _                 => None
      }
    }
  }

  /**
    * direction extractor,
    * semantically, just a function (Loc, Board) => Option[Int]
    */
  object PawnMove1 {
    def unapply(args: (Loc, Board)) = args match {
      case PawnWhite(dy) => Some(dy * 1)
      case PawnBlack(dy) => Some(dy * 1)
      case _             => None
    }
  }

  /**
    * direction extractor,
    * semantically, just a function (Loc, Board) => Option[Int]
    */
  object PawnMove2 {
    def unapply(as: (Loc, Board)) = as match {
      case PawnWhite(dy) if as._1.y == 2 => Some(dy * 2)
      case PawnBlack(dy) if as._1.y == 7 => Some(dy * 2)
      case _                             => None
    }
  }

  // one step forward
  def mvPawn1(l: Loc, b: Board) = (l, b) match {
    case PawnMove1(dy) => l.move(0, dy)
    case _             => None
  }

  // two steps forward
  def mvPawn2(l: Loc, b: Board) = (l, b) match {
    case PawnMove2(dy) => l.move(0, dy)
    case _             => None
  }

  // 1 + 2 steps forward
  def mvPawnFwd(l: Loc, b: Board) = Seq(
    Seq(mvPawn1(l, b), mvPawn2(l, b)).flatten
  )

  def mvPawnBiteL(l: Loc, b: Board) = (l, b) match {
    case PawnMove1(dy) => l.move(-1, dy)
    case _             => None
  }

  def mvPawnBiteR(l: Loc, b: Board) = (l, b) match {
    case PawnMove1(dy) => l.move(+1, dy)
    case _             => None
  }

  def mvPawnBite(l: Loc, b: Board) =
    Seq(mvPawnBiteL(l, b), mvPawnBiteR(l, b))
      .flatten
      .map(Seq(_))

  /**
    * get the color of pawn for vector
    * one forward
    * + (opt) forward
    * + (diagonal) bite left + bite right
    * + white/black different directions
    */
  def mvPawn(l: Loc, b: Board) = mvPawnFwd(l, b) ++ mvPawnBite(l, b)

}
