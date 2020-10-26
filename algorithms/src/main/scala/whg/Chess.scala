package whg

/**
  * - possible move for each figure
  *   - general
  *   - filtered by occupied cells and cells on board
  * - "check"
  *
  */
case class Chess(private val board: Board, nextC: Color, checkBlack: Boolean = false, checkWhite: Boolean = false) {

  def nextTurn(b: Board) = copy(board = b, nextC = nextC.another)
  def chWhite(ch: Boolean) = copy(checkWhite = ch)
  def chBlack(ch: Boolean) = copy(checkBlack = ch)
  
  def validate(m: Move): Either[String, Move] =
    Some(m)
      .filter(m => board.isColorAt(m.start, nextC)) 
      .toRight(s"cell ${m.start} has the wrong color: $nextC")
      .flatMap(m => board.at(m.start).get.validateMove(m, board))

  def move(m: String): (Chess, Option[String]) =
    Move.parse(m)                     // Option[Move]
      .toRight(s"error parsing `$m`") // Either[String, Move]
      .flatMap(validate)               // Either[String, Move]
      .map(board.move)                // Either[String, (Board, Boolean)]
      .fold (
        msg => (this, Some(msg)),
        { case (b, _) => (nextTurn(b), None) }
      )
      
  def rep = board.rep
  
  def play(turns: Seq[String]): Unit = turns match {
    case Nil        => ()
    case turn::tail => 
      println(s"${Console.WHITE}$nextC${Console.RESET} is going to make turn ${Console.BLUE}$turn${Console.RESET}")
      val (chess2, message) = move(turn)
      println(chess2.board.rep)
      message.foreach(m => println(s"${Console.RED}message:${Console.RESET} $m"))
      if (chess2.checkWhite) println("WHITE: CHECK") 
      if (chess2.checkBlack) println("BLACK: CHECK") 
      println
      chess2.play(tail)
  }
}

object Chess {
  def initial = new Chess(Board.initial, White)
}
