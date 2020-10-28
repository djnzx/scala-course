package whg

sealed abstract class CFigure(val c: Color, private val s: Char) {
  import Directions._
  import ExceptionSyntax._

  def rep: Char = c match {
    case White => s.toUpper
    case Black => s.toLower
  }
  
  /** required move is a pawn and it's in forward direction */
  object IsPawnFwd {
    def unapply(as: (Move, Board)) = as match { case (m, b) =>
      Some(m.finish)
        .filter(fi => mvPawnFwd(m.start, b).exists(_.contains(fi)))
    }
  }

  /** required move is a pawn and it's in bite (L or R) direction */
  object IsPawnBite {
    def unapply(as: (Move, Board)) = as match { case (m, b) =>
      Some(m.finish)
        .filter(fi => mvPawnBite(m.start, b).exists(_.contains(fi)))
    }
  }

  /** available moves from current point grouped by directions
    * for further checking whether path is empty */
  def nextFrom(l: Loc, b: Board) = this match {
    case _: Pawn   => mvPawn(l, b)
    case _: Queen  => mvQueen(l)
    case _: King   => mvKing(l)
    case _: Bishop => mvBishop(l)
    case _: Knight => mvKnight(l)
    case _: Rook   => mvRook(l)
  }

  def validateMove(move: Move, b: Board) = {
    val colorOppositeMe = b.at(move.start).get.c.another
    def isOppositeAt(l: Loc) = b.isColorAt(l, colorOppositeMe)
    def isFreeAt(l: Loc) = b.isFreeAt(l)

    def checkTargetInPossible(m: Move) =
      nextFrom(m.start, b)
        .find(_.contains(m.finish))
        .toRight(ImIFMTargetNotInList(m))

    /**
      * for pawn forward move - target must be empty
      * for pawn bite move - target must be of opposite color
      * for any other move - target must be empty or of opposite color
      */
    def checkTargetColor(path: Seq[Loc]) = {
      def wrap(cond: Boolean, left: InvalidMove) =
        Either.cond(cond, path, left)
      
      this match {
        case _: Pawn => (move, b) match {
          case IsPawnFwd(fi)  => wrap(isFreeAt(fi), ImIFMTargetIsNotClean(move))
          case IsPawnBite(fi) => wrap(isOppositeAt(fi), ImIFMTargetIsNotOpposite(move))
          case _             => !msg.byDesign
        }
        case _ => wrap(isFreeAt(move.finish) || isOppositeAt(move.finish), ImIFMTargetIsNotCleanOrOpp(move))
      }
    }

    /**
      * checks that all the cells on the way to target are CLEAN
      * we don't need to consider special cases
      * for king/knight/pawn bite
      * since their sub-sequences are empty
      */
    def checkPathIsClean(path: Seq[Loc]) =
      Either.cond(path.takeWhile(_ != move.finish).forall(isFreeAt),
        move,
        ImIFMPathIsNotClean(move)
      )

    /**
      * start point validation is done in Chess class,
      * because it needs to access current color
      */
    Right(move)
      .flatMap(checkTargetInPossible)
      .flatMap(checkTargetColor)
      .flatMap(checkPathIsClean)
  }
}

final case class Pawn(color: Color) extends CFigure(color, 'p')
final case class Queen(color: Color) extends CFigure(color, 'q')
final case class King(color: Color) extends CFigure(color, 'k')
final case class Bishop(color: Color) extends CFigure(color, 'b')
final case class Knight(color: Color) extends CFigure(color, 'n')
final case class Rook(color: Color) extends CFigure(color, 'r')
