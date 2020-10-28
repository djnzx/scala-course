package whg

import Utils._

sealed trait InvalidMove extends Product {
  def rep: String = this match {
    case ImErrorParsingLocation(loc)   => msg.errorParsingLocation(loc)
    case ImErrorParsingMove(move)      => msg.errorParsingMove(move)
    case ImStartCellIsEmpty(m)         => msg.startCellIsEmpty(m)
    case ImWrongColorAtStartCell(m, c) => msg.wrongColorAtStartCell(m, c)
    case ImInvalidMoveInCheck(m, c)    => msg.invalidMoveInCheck(m, c)
    case ImInvalidFigureMove(m)        => msg.invalidFigureMove(m)
  }
  def die = wrong(rep)
}
case class ImErrorParsingLocation(loc: String) extends InvalidMove
case class ImErrorParsingMove(move: String) extends InvalidMove
case class ImStartCellIsEmpty(m: Move) extends InvalidMove
case class ImWrongColorAtStartCell(m: Move, c: Color) extends InvalidMove
case class ImInvalidMoveInCheck(m: Move, c: Color) extends InvalidMove
case class ImInvalidFigureMove(m: Move) extends InvalidMove