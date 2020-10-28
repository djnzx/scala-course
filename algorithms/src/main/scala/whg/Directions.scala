package whg

object Directions {
  /** move to (dx, dy) and filter */
  val maf = (deltas: Seq[(Int, Int)]) => (l: Loc) => deltas.flatMap { case (dx, dy) => l.move(dx, dy) }
  /** apply delta and filter */
  val aaf = (f: Int => Loc => Option[Loc]) => (l: Loc) => (1 to 7).flatMap(d => f(d)(l))
  /** values to produce deltas */
  val kgVals = IndexedSeq(-1, 0, 1)
  val knVals = IndexedSeq(-2, -1, 1, 2)
  /** deltas: (dx, dy) 
    * for King */
  val kgDeltas = for {
    x <- kgVals
    y <- kgVals
    if !(x == 0 && y == 0)
  } yield (x, y)
  /** for Knight */
  val knDeltas = for {
    x <- knVals
    y <- knVals
    if math.abs(x) != math.abs(y)
  } yield (x, y)
  val kgMoves = maf(kgDeltas)
  val knMoves = maf(knDeltas)
  
  val toR  = aaf { d => _.move( d, 0) }
  val toL  = aaf { d => _.move(-d, 0) }
  val toU  = aaf { d => _.move(0,  d) }
  val toD  = aaf { d => _.move(0, -d) }
  val toRU = aaf { d => _.move( d,  d) }
  val toLU = aaf { d => _.move(-d,  d) }
  val toRD = aaf { d => _.move( d, -d) }
  val toLD = aaf { d => _.move(-d, -d) }

  def mvRook(l: Loc) = Seq(toL(l), toR(l), toU(l), toD(l))
  def mvBishop(l: Loc) = Seq(toLU(l), toLD(l), toRU(l), toRD(l))
  def mvQueen(l: Loc) = mvRook(l) ++ mvBishop(l)
  def mvKnight(l: Loc) = knMoves(l).map(Seq(_))
  def mvKing(l: Loc) = kgMoves(l).map(Seq(_))

  /**
    * direction extractor for White Pawn
    * @return Some(1) if board(loc) == white
    */
  object IsWhite {
    def unapply(as: (Loc, Board)) = as match { case (l, b) =>
      b.isWhiteAt(l) match {
        case true => Some(+1)
        case _    => None
      }
    }
  }

  /**
    * direction extractor for White Pawn
    * @return Some(-1) if board(loc) == black
    */
  object IsBlack {
    def unapply(as: (Loc, Board)) = as match { case (l, b) =>
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
    def unapply(as: (Loc, Board)) = as match {
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
    * both of them must me wrapped into one Seq, because they are on the same direction
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
    * each of them must me wrapped into own Seq, since directions are different
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
