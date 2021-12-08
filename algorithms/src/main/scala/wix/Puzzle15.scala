package wix

import wix.Board.TBoard

import scala.util.Random

sealed trait ValidationError
case class CellNotOnTheBoard(x: Int, y: Int) extends ValidationError
case class CellIsEmpty(x: Int, y: Int) extends ValidationError
case class CellHasNoAdjacentFree(x: Int, y: Int) extends ValidationError

trait Board {
  def isOnBoard(x: Int, y: Int): Boolean = x >= 0 && x <= 3 && y >= 0 && y <= 3
  def isFreeAt(x: Int, y: Int): Boolean
  def isOccupiedAt(x: Int, y: Int): Boolean = !isFreeAt(x, y)
  def validate(x: Int, y: Int): Either[ValidationError, (Int, Int)]
  def move(x: Int, y: Int): Either[ValidationError, Board]
  def isDone: Boolean
  def printMe(): Unit
}

object Board {
  type TCell = Option[Int]
  type TRow = Vector[TCell]
  type TBoard = Vector[TRow]

  def wrapAndGroup(xs: Seq[Int]): TBoard =
    xs
      .toVector
      .map {
        case 0 => None
        case n => Some(n)
      }
      .grouped(4)
      .toVector

  val cellsRandom: TBoard = wrapAndGroup(Random.shuffle((0 to 15).toVector))
  val cellsSolved: TBoard = wrapAndGroup((1 to 15) :+ 0)

  def shuffled: Board = new BoardImpl(cellsRandom)
  def solved: Board = new BoardImpl(cellsSolved)
  def apply(): Board = shuffled
}

class BoardImpl(board: TBoard) extends Board {
  override def printMe(): Unit = {
    val line = "-" * (4 * 4 + 5) + "\n"
    val data = board
      .map {
        _.map {
          case None    => "    "
          case Some(n) => " %2d ".format(n)
        }
          .mkString("|", "|", "|") + "\n"
      }
      .mkString(line, line, line)

    print(data)
  }
  override def isDone: Boolean = board == Board.cellsSolved

  override def isFreeAt(x: Int, y: Int): Boolean = isOnBoard(x, y) && board(y)(x).isEmpty

  val deltas = Seq((-1, 0), (1, 0), (0, -1), (0, 1))

  def emptyAdjTo(x: Int, y: Int): Option[(Int, Int)] =
    deltas
      .map { case (dx, dy) => (x + dx) -> (y + dy) }
      .find { case (x, y) => isFreeAt(x, y) }

  override def validate(x: Int, y: Int): Either[ValidationError, (Int, Int)] =
    Right(x, y)
      .flatMap { case (x, y) => Either.cond(isOnBoard(x, y), (x, y), CellNotOnTheBoard(x, y)) }
      .flatMap { case (x, y) => Either.cond(isOccupiedAt(x, y), (x, y), CellIsEmpty(x, y)) }
      .flatMap { case (xi, yi) => emptyAdjTo(xi, yi).toRight(CellHasNoAdjacentFree(x, y)) }

  private def doSwap(x1: Int, y1: Int, x2: Int, y2: Int): Board = new BoardImpl(
    board
      .updated(y2, board(y2).updated(x2, board(y1)(x1)))
      .updated(y1, board(y1).updated(x1, None)),
  )

  override def move(x: Int, y: Int): Either[ValidationError, Board] =
    validate(x, y)
      .map { case (x2, y2) => doSwap(x, y, x2, y2) }
}

object BoardTest extends App {
  // shuffled representation
  val b1 = Board()
  // solved representation
  val b2 = Board.solved

  // isDone
  b1.printMe()
  println(b1.isDone)
  b2.printMe()
  println(b2.isDone)

  // validations
  println(b2.validate(4, -5)) // Left(CellNotOnTheBoard(4,-5))
  println(b2.validate(3, 3)) // Left(CellIsEmpty(3,3))
  println(b2.validate(2, 2)) // Left(CellHasNoAdjacentFree(2,2))
  println(b2.validate(2, 3)) // Right((3,3))
  println(b2.validate(3, 2)) // Right((3,3))

  // moves
  b2.move(3, 2)
    .foreach { b3 =>
      b3.printMe()
      b3.move(3, 1)
        .foreach(_.printMe())
    }
}
