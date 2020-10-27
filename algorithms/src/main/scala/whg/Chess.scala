package whg

/**
  * - possible move for each figure
  *   - general
  *   - filtered by occupied cells and cells on board
  * - "check"
  *
  */
case class Chess(private val board: Board, nextC: Color, check: Option[Color] = None) {

  def nextMove(b: Board) = copy(board = b, nextC = nextC.another)
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
      .flatMap(board.move)             // Either[String, Board(new)]
      .flatMap { b =>                  // Either[String, Board(new)]
        // if nextC was under the "check" we need to clear it from that color
        // None folds to true
        // for Some(White) we run isUnderTheCheck(..., White).
        // we need to get false from isUnderTheCheck
        Check.foldCheck(check, color => !Check.isUnderTheCheck(b, color)) match {
          case true  => Right(b)                                          // "check" was absent or cleared
          case false => Left(s"Invalid, $nextC is still under the CHECK") // still in "check"
        }
      }
      .fold (
        msg => (this,                 Some(msg)), // same board + error message
        b   => (nextMove(b).chNone(), None)       // new board, switched color, cleared "check"
      )
      
  def rep = board.rep

  def play(turns: Iterator[String]): Chess = Chess.play(turns, this)

  def play(turns: Iterable[String]): Chess = play(turns.iterator)

  /** filename from the file located in the resources folder */
  def play(fileName: String): Chess = play(ChessIterator.resource(fileName).map(Move.fromGiven))
}

object Chess {
  import fansi.{Back => BG, Color => FG}
  
  def initial = new Chess(Board.initial, White)
  
  def printLine() = println("-----------------------")
  
  def encolorColor(c: Color) = {
    val cs = s"  $c  "
    c match {
      case White => BG.Black(FG.DarkGray(cs))
      case Black => BG.White(FG.LightGray(cs))
    }
  }

  def beforeMove(chess: Chess, move: String) = {
    val cs = encolorColor(chess.nextC)
    val moves = FG.Blue(move)
    println(s"$cs is going to make a move: $moves")
  }

  def afterMove(chess2: Chess, message: Option[String]) = {
    val msg = FG.Red("message:")
    println(chess2.board.rep)
    message.foreach(m => println(s"$msg $m"))
    chess2.check.foreach(c => println(s"$c: CHECK"))
    printLine()
  }

  def makeMove(chess: Chess, move: String) = {
    beforeMove(chess, move)
    val (chess2, message) = chess.move(move)
    afterMove(chess2, message)
    chess2
  }
  
  def play(turns: Iterator[String], ch: Chess = initial) = turns.foldLeft(ch) { makeMove }
  
  def play(fileName: String) = {
    val c = Chess.initial
    println(c.rep)
    Chess.printLine()
    c.play(fileName)
  }
  
}
