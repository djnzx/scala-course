package whg

object ChessApp extends App {
  
  val chessIt = ChessIterator.resource("chess.txt")
  chessIt.foreach(x => println(x.mkString("Array(", ", ", ")")))
  
}
