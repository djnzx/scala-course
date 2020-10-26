package whg

import whg.Tools.wrongState

/**
  * represents location
  * @param x a-h -coordinate
  *          
  *          abcdefgh => 12345678
  * @param y 1-8 -coordinate
  *          
  *          12345678 => 12345678
  */
case class Loc private (x: Int, y: Int) {
  def isOnBoard = Loc.isOnBoard(x) && Loc.isOnBoard(y)
  def move(dx: Int, dy: Int) = Loc.move(this, dx, dy)
  def >(dst: Loc) = Move(this, dst)
  override def toString: String = {
    import Loc.{xToL, yToD}
    s"${xToL(x)}${yToD(y)}"
  }
}

object Loc {
  def apply(loc: String): Loc = parseOrEx(loc)
  def xToL(i: Int): Char = (i + 'a' - 1).toChar
  def yToD(i: Int): Char = (i + '0').toChar
  def iToX(i: Int) = i - 'a' + 1
  def iToY(i: Int) = i - '1' + 1
  def isOnBoard(a: Int) = a >= 1 && a <= 8
  def parse(loc: String) = Option(loc.trim)
    .filter(_.length == 2)
    .map(_.toCharArray)
    .map { case Array(x, y) => (iToX(x), iToY(y)) }
    .filter { case (x, y) => isOnBoard(x) && isOnBoard(y) }
    .map { case (x, y) => Loc(x, y) }
  def parseOrEx(loc: String) = parse(loc)
    .getOrElse(wrongState(s"Wrong location given: `$loc`"))
  // automatically get rid of Location if out of the board
  def move(l: Loc, dx: Int, dy: Int) = Option(l)
    .map { case Loc(x, y) => Loc(x + dx, y + dy) }
    .filter(_.isOnBoard)
  def a(y: Int) = Loc(1, y)
  def b(y: Int) = Loc(2, y)
  def c(y: Int) = Loc(3, y)
  def d(y: Int) = Loc(4, y)
  def e(y: Int) = Loc(5, y)
  def f(y: Int) = Loc(6, y)
  def g(y: Int) = Loc(7, y)
  def h(y: Int) = Loc(8, y)
}

case class Move private (start: Loc, finish: Loc)

object Move {
  def apply(move: String): Move = parseOrEx(move)
  def parse(move: String) = Option(move.trim)
    .filter(_.length == 4)
    .map(_.splitAt(2))
    .flatMap { case (s1, s2) => for {
      x <- Loc.parse(s1)
      y <- Loc.parse(s2)
    } yield Move(x, y) }

  private def xx(x: Int) = (x + 'a').toChar
  private def yy(y: Int) = (7 - y + '1').toChar
  /** array has length 4 because of underline given implementation */
  def fromGiven(move: Array[Int]) = move match {
    case Array(w,x,y,z) => s"${xx(w)}${yy(x)}${xx(y)}${yy(z)}"
  }

  def parseOrEx(move: String) = parse(move)
    .getOrElse(wrongState(s"Wrong move given: `$move"))
}
