package knight

import scala.collection.Searching

object KnightMoves extends App {

  def isInRange(x: Int) = x >= 0 && x <= 7
  def isOnBoard(loc: String) = isInRange(loc(0) - 'a') && isInRange(loc(1) - '1')
  def move1(c: Char, d: Int) = (c + d).toChar.toString
  def move(loc: String, dx: Int, dy: Int) = move1(loc(0), dx) + move1(loc(1), dy)
  val deltas = List(1, 2).flatMap(x => List(-x, x))
  def moves(loc: String) =
    deltas
      .flatMap(dx => deltas.map(dy => (dx, dy)))
      .filter { case (dx, dy) => math.abs(dx) != math.abs(dy) }
      .map { case (dx, dy) => move(loc, dx, dy) }
      .filter(isOnBoard)

  moves("a1").foreach(println)

  Array(1,2,3).search(2) match {
    case Searching.Found(foundIndex) => println(s"found at: ${foundIndex}")
    case Searching.InsertionPoint(insertionPoint) => println(s"should be inserted at ${insertionPoint}")
  }

}
