package sudoku

import cats.effect.Deferred
import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import sudoku.Solver.Value

trait Solver[F[_]] {
  def solve(givens: List[Value.Given]): F[List[Value]]
}

object Solver {

  case class Coord(r: Int, c: Int)

  trait CoordSyntax {
    implicit class CoordOps(p: Coord) {
      def isPeerOf(that: Coord): Boolean = notMe(that) && (sameRow(that) || sameCol(that) || sameBox(that))
      private def sameRow(that: Coord) = p.r == that.r
      private def sameCol(that: Coord) = p.c == that.c
      private def sameBox(that: Coord) = (p.r / 3 == that.r / 3) && (p.c / 3 == that.c / 3)
      private def notMe(that: Coord) = p != that
    }
  }

  object Coord extends CoordSyntax {
    val rIndices: List[Int] = (0 to 8).toList
    val cIndices: List[Int] = (0 to 8).toList
    val allCoords: List[Coord] = rIndices.flatMap(r => cIndices.map(c => Coord(r, c)))
  }

  sealed trait Value {
    val coord: Coord
    val value: Int
  }

  object Value {
    case class Given(coord: Coord, value: Int) extends Value
  }

  sealed trait Candidate {
    val coord: Coord
  }

  object Candidate {
    class Single private[Candidate] (override val coord: Coord, override val value: Int) extends Candidate with Value
    class Multiple private[Candidate] (override val coord: Coord, val candidates: Set[Int]) extends Candidate

    trait MultipleSyntax {
      implicit class MultipleOps(m: Multiple) {
        def refine(peer: Value): Candidate = {
          val newValues: Set[Int] = m.candidates -- Option.when(m.coord.isPeerOf(peer.coord))(peer.value)
          newValues.toList match {
            case Nil           => throw new IllegalStateException("unreachable")
            case single :: Nil => new Single(m.coord, single)
            case multiple      => new Multiple(m.coord, multiple.toSet)
          }
        }
      }
    }
    object Multiple extends MultipleSyntax

    def initial(coord: Coord): Multiple = new Multiple(coord, (1 to 9).toSet)
  }

  trait Cell {
    def coord: Coord // Abstract
    protected[this] def deferredValue: Deferred[IO, Value] // Abstract
    def deduceSingleCandidate(allCells: List[Cell]): IO[Value] // Abstract

    def getValue: IO[Value] = deferredValue.get
    def solve(givensMap: Map[Coord, Value.Given], allCells: List[Cell]): IO[Value] =
      givensMap
        .get(coord)
        .fold(deduceSingleCandidate(allCells))(_.pure[IO])
        .flatTap(v => deferredValue.complete(v))
  }

  def toString(list: List[Value]): String = {
    val board: Map[Coord, Int] = list.map(cs => cs.coord -> cs.value).toMap

    val NL = "\n"
    val L = "| "
    val C = " | "
    val R = " |"
    val rowSep = s"+-------+-------+-------+"

    Coord.rIndices
      .map(r =>
        Coord.cIndices
          .map(c => board.get(Coord(r, c)).fold("_")(_.toString))
          .grouped(3)
          .map(_.mkString(" "))
          .mkString(L, C, R)
      )
      .grouped(3)
      .map(_.grouped(3).map(_.mkString(NL)))
      .map(_.mkString + s"$NL$rowSep")
      .mkString(s"$rowSep$NL", NL, "")
  }

}
