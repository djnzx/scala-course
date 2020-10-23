package whg

sealed abstract class CFigure(val c: Color, private val s: Char) extends Product  {
  import Directions._
  
  def rep: Char = c match {
    case White => s.toUpper
    case Black => s.toLower
  }
  
  /** 
    * source color validation inside the Chess class
    * {{{nextFrom(l: Loc, b: Board): Seq[Seq[Length]]}}}
    * TODO: 2. target
    * {{{isPathClean}}}
    */
  def validateMove(m: Move, b: Board): Either[String, Move] = {
    val figMy: CFigure = b.at(m.start).get
    val colorMy = figMy.c
    val colorOpp = Color.another(colorMy)
    // just aliases to the board
    def isOppositeAt(l: Loc) = b.isColorAt(l, colorOpp)
    def isFreeAt(l: Loc) = b.isFreeAt(l)
    // we don't need to bother to validate king/knight moves since their seq's are empty
    def checkPathClean(path: Seq[Loc]) = path.takeWhile(_ != m.finish).forall(isFreeAt)
    // check target color
    def checkTarget = this match {
      case Pawn(c) => ???
      case _:Queen |
           _:King |
           _:Bishop |
           _:Knight |
           _:Rook => isFreeAt(m.finish) || isOppositeAt(m.finish)
    }

    Some(m.finish)
      .flatMap(fi => nextFrom(m.start, b).find(_.contains(fi))) // vector with direction if found
      .filter(checkPathClean)                                // path is clean
      .filter(_ => checkTarget)                              // target is clean or opposite
      .map(_ => m)
      .toRight("target cell isn't empty or has your color")
  }
  
  // TODO: consider to represent as Set to make contains O(1) ???
  private def nextFrom(l: Loc, b: Board) = this match {
    case _:Pawn   => mvPawn(l, b)
    case _:Queen  => mvQueen(l)
    case _:King   => mvKing(l)
    case _:Bishop => mvBishop(l)
    case _:Knight => mvKnight(l)
    case _:Rook   => mvRook(l)
  }
}

final case class Pawn(color: Color) extends CFigure(color, 'p')   // Piece, Пешка
final case class Queen(color: Color) extends CFigure(color, 'q')
final case class King(color: Color) extends CFigure(color, 'k')
final case class Bishop(color: Color) extends CFigure(color, 'b') // Слон
final case class Knight(color: Color) extends CFigure(color, 'n') // Конь
final case class Rook(color: Color) extends CFigure(color, 'r')   // Ладья
