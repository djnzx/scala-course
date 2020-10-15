package google.live.t2

import google.live.t1.T1Domain.nextTo
import tools.Timed.printTimed

/**
  * Gold miner
  * Plain Recursive Implementation
  * 
  * Exponential
  * 7 * 7 - 0.5 - 10s depends whether our matrix sparse or dense
  */
object T2PlainRecursion extends App {
  val rows = 3
  val cols = 3 
  case class Pt(x: Int, y: Int)
  def line(n: Int = cols) = Array.fill[Int](n)(nextTo(6) + 0)
  type Board = Array[Array[Int]]
  val board = Array.fill(rows)(line())
  def isInRange(a: Int, lo: Int, hi: Int) = a >= lo && a < hi
  def isOnBoard(p: Pt) = isInRange(p.y, 0, board.length) && isInRange(p.x, 0, board(0).length)
  def at(p: Pt): Int = board(p.y)(p.x)
  def mark(p: Pt, value: Int) = board(p.y)(p.x) = value
  def isUnvisited(p: Pt) = at(p) > 0
  val allowed = Seq((0,1), (0,-1), (1,0), (-1,0))
  def move(p: Pt)(dx: Int, dy: Int) = Pt(p.x + dx, p.y + dy)
  def nextMove(p: Pt) = allowed.map((move(p) _).tupled).filter(isOnBoard)
  def nextUnvisited(p: Pt) = nextMove(p).filter(isUnvisited)
  def pb = board.foreach(a => println(a.mkString(" ")))

  def nextStep(p: Pt, sum: Int): Int =
    nextUnvisited(p) match {
      case Nil => sum + at(p)
      case nxt =>
        val amt = at(p)
        mark(p, 0)
        val mx = nxt.map(nextStep(_, sum + amt)).max
        mark(p, amt)
        mx
    }
  
  def maxFrom(p: Pt): Int = nextStep(p, 0)

  pb
  printTimed(maxFrom(Pt(1,1)))
}
