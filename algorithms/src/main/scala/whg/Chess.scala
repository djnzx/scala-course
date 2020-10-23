package whg

/**
  * TODO:
  *
  * - possible move for each figure
  *   - general
  *   - filtered by occupied cells and cells on board
  * - "check" ("шах")
  * - mirroring for opposite side for Pawn
  *
  */
case class Chess(private val board: Board, nextC: Color) {

  def nextTurn(b: Board) = copy(board = b, nextC = Color.another(nextC))
  
  /** validation of figure color */
  def validate(m: Move): Either[String, Move] =
    Some(m)
      .filter(m => board.isColorAt(m.start, nextC)) 
      .toRight(s"cell ${m.start} has the wrong color: ${nextC}")
      .flatMap(m => board.at(m.start).get.validateMove(m, board))

  /**                new state [Error message, Check]
    *                    ||            ||       ||
    *                    v             V        V    */
  def move(m: String): (Chess, Either[String, Boolean]) =
    Move.parse(m)                     // Option[Move]
      .toRight(s"error parsing `$m`") // Either[String, Move]
      .flatMap(validate)               // Either[String, Move]
      .map(board.move)                // Either[String, (Board, Boolean)]
      .fold (
        msg => (this, Left(msg)),
        { case (b, _) => (nextTurn(b), Right(false)) } // TODO: false means "CHECK"
      )
}

object Chess {
  def initial = new Chess(Board.initial, White)
}
