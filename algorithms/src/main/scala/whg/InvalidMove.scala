package whg

import ExceptionSyntax._

sealed trait InvalidMove {
  def rep: String = this match {
    case ImErrorParsingLocation(loc)   => msg.errorParsingLocation(loc)
    case ImErrorParsingMove(move)      => msg.errorParsingMove(move)
    case ImStartCellIsEmpty(m)         => msg.startCellIsEmpty(m)
    case ImWrongColorAtStartCell(m, c) => msg.wrongColorAtStartCell(m, c)
    case ImInvalidMoveInCheck(m, c)    => msg.invalidMoveInCheck(m, c)
    case ImIFMTargetNotInList(m)       => msg.targetNotInList(m)
    case ImIFMPathIsNotClean(m)        => msg.pathIsNotClean(m)
    case ImIFMTargetIsNotClean(m)      => msg.targetIsNotClean(m)
    case ImIFMTargetIsNotOpposite(m)   => msg.targetIsNotOpposite(m)
    case ImIFMTargetIsNotCleanOrOpp(m) => msg.targetIsNotCleanOrOpp(m)
  }
  def die = !rep
}
// parsing errors
case class ImErrorParsingLocation(loc: String) extends InvalidMove
case class ImErrorParsingMove(move: String) extends InvalidMove
// chess board error
case class ImStartCellIsEmpty(m: Move) extends InvalidMove
case class ImWrongColorAtStartCell(m: Move, c: Color) extends InvalidMove
case class ImInvalidMoveInCheck(m: Move, c: Color) extends InvalidMove
// invalid figure move errors
case class ImIFMTargetNotInList(m: Move) extends InvalidMove
case class ImIFMPathIsNotClean(m: Move) extends InvalidMove
case class ImIFMTargetIsNotClean(m: Move) extends InvalidMove
case class ImIFMTargetIsNotOpposite(m: Move) extends InvalidMove
case class ImIFMTargetIsNotCleanOrOpp(m: Move) extends InvalidMove