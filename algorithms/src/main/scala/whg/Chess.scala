package whg

import fansi.Str

/**
  * - possible move for each figure
  *   - general
  *   - filtered by occupied cells and cells on board
  * - "check"
  *
  */
case class Chess(private val board: Board, nextC: Color, check: Option[Color] = None) {

  def nextTurn(b: Board) = copy(board = b, nextC = nextC.another)
  def chNone() = copy(check = None)
  def chBlack() = copy(check = Some(Black))
  def chWhite() = copy(check = Some(White))
  
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
      val color: Str = fansi.Color.White(nextC.toString)
      val turns = fansi.Color.Blue(turn)
      val msg = fansi.Color.Red("message:")
      
      println(s"$color is going to make turn $turns")
      val (chess2, message) = move(turn)
      println(chess2.board.rep)
      message.foreach(m => println(s"$msg $m"))
      chess2.check.foreach(c => println(s"$c: CHECK"))
      println
      chess2.play(tail)
  }
  
  def play(fileName: String) = {
    // read file
    // run play(turns: Seq[String])
  }
}

object Chess {
  def initial = new Chess(Board.initial, White)
}
