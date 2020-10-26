package whg

object Check {

  def isWhiteUnderTheCheck(b: Board) = isUnderTheCheck(b, White)
  def isBlackUnderTheCheck(b: Board) = isUnderTheCheck(b, Black)
  // TODO
  def isUnderTheCheck(b: Board, c: Color): Boolean = {
    // find the king on the board
    // check all directions
    // + check knights
    ???
  }
  
}
