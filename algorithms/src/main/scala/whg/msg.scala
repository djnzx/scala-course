package whg

object msg {
  def errorParsingLocation(loc: String) = s"Error parsing location: `$loc`."
  def errorParsingMove(move: String) = s"Error parsing move: `$move`."
  def wrongColorAtStartCell(m: Move, c: Color) = s"Invalid move: `$m`. cell `${m.start}` has the wrong color: $c, expected to be ${c.another}."
  def invalidMoveInCheck(m: Move, c: Color) = s"Invalid move: `$m`. $c is still in CHECK."
  def startCellIsEmpty(m: Move) = s"Invalid move: `$m`. Start cell `${m.start}` is empty."
  def invalidFigureMove(m: Move) = s"Invalid move: `$m`. Target cell isn't empty or has your color, or path isn't clean"
  
  def noKing(c: Color) = s"Error. There is no king of color $c"
  def fileNotFound(name: String) = s"File `$name` isn't found in resources folder"
  val done = "Done!"
}
