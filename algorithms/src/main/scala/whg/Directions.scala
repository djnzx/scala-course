package whg

object Directions {
  val oneVals = IndexedSeq(-1, 0, 1)
  val oneDeltas = for {
    x <- oneVals
    y <- oneVals
    if !(x == 0 && y == 0)
  } yield (x, y)
  val knVals = IndexedSeq(-2, -1, 1, 2)
  val knDeltas = for {
    x <- knVals
    y <- knVals
    if math.abs(x) != math.abs(y)
  } yield (x, y)
  def moveAndFilter(l: Loc, deltas: Seq[(Int, Int)]) =
    deltas.flatMap { case (dx, dy) => l.move(dx, dy) }
  /** apply and filter */
  def aaf(l: Loc)(f: (Loc, Int) => Option[Loc]) =
    (1 to 7).flatMap(d => f(l, d))
  
  def toR(l: Loc)  = aaf(l) { case (l, d) => l.move( d, 0) }
  def toL(l: Loc)  = aaf(l) { case (l, d) => l.move(-d, 0) }
  def toU(l: Loc)  = aaf(l) { case (l, d) => l.move(0,  d) }
  def toD(l: Loc)  = aaf(l) { case (l, d) => l.move(0, -d) }
  def toRU(l: Loc) = aaf(l) { case (l, d) => l.move( d,  d) }
  def toLU(l: Loc) = aaf(l) { case (l, d) => l.move(-d,  d) }
  def toRD(l: Loc) = aaf(l) { case (l, d) => l.move( d, -d) }
  def toLD(l: Loc) = aaf(l) { case (l, d) => l.move(-d, -d) }

  def mvKing(l: Loc) = moveAndFilter(l, oneDeltas).map(Seq(_))
  def mvRook(l: Loc) = Seq(toL(l), toR(l), toU(l), toD(l))
  def mvBishop(l: Loc) = Seq(toLU(l), toLD(l), toRU(l), toRD(l))
  def mvQueen(l: Loc) = mvRook(l) ++ mvBishop(l)
  def mvKnight(l: Loc) = moveAndFilter(l, knDeltas).map(Seq(_))

  /**
    * direction extractor for White Pawn
    * @return Some(1) if board(loc) == white pawn
    */
  object IsWhite {
    def unapply(args: (Loc, Board)) = args match { case (l, b) =>
      b.isWhiteAt(l) match {
        case true => Some(+1)
        case _    => None
      }
    }
  }

  /**
    * direction extractor for White Pawn
    * @return Some(-1) if board(loc) == black pawn
    */
  object IsBlack {
    def unapply(args: (Loc, Board)) = args match { case (l, b) =>
      b.isBlackAt(l) match {
        case true => Some(-1)
        case _    => None
      }
    }
  }

  /**
    * direction extractor,
    * semantically, just a function (Loc, Board) => Option[Int]
    */
  object PawnMove1 {
    def unapply(args: (Loc, Board)) = args match {
      case IsWhite(dy) => Some(dy * 1)
      case IsBlack(dy) => Some(dy * 1)
      case _             => None
    }
  }

  /**
    * direction extractor,
    * semantically, just a function (Loc, Board) => Option[Int]
    */
  object PawnMove2 {
    def unapply(as: (Loc, Board)) = as match {
      case IsWhite(dy) if as._1.y == 2 => Some(dy * 2)
      case IsBlack(dy) if as._1.y == 7 => Some(dy * 2)
      case _                             => None
    }
  }

  /**
    * ONE step forward, if possible.
    * picking the right direction based on the color
    * taking into account only color
    * @return Option[Loc] - if possible, None - if not
    */
  def mvPawn1(l: Loc, b: Board) = (l, b) match {
    case PawnMove1(dy) => l.move(0, dy)
    case _             => None
  }

  /**
    * TWO steps forward, if possible.
    * picking the right direction based on the color
    * taking into account only color
    * @return Option[Loc] - if possible, None - if not
    */
  def mvPawn2(l: Loc, b: Board) = (l, b) match {
    case PawnMove2(dy) => l.move(0, dy)
    case _             => None
  }

  /**
    * ONE and TWO steps forward, if possible.
    * picking the right direction based on the color
    * taking into account only color
    */
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

  /**
    * mvPawnBite = mvPawnBiteL + mvPawnBiteR
    */
  def mvPawnBite(l: Loc, b: Board) =
    Seq(mvPawnBiteL(l, b), mvPawnBiteR(l, b))
      .flatten
      .map(Seq(_))

  /**
    * mvPawn = mvPawnFwd + mvPawnBite
    */
  def mvPawn(l: Loc, b: Board) = mvPawnFwd(l, b) ++ mvPawnBite(l, b)

}
