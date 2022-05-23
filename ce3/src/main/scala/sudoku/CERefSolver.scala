package sudoku

import cats.effect.Deferred
import cats.effect.IO
import cats.effect.Ref
import cats.implicits._
import sudoku.Solver.Candidate
import sudoku.Solver.Cell
import sudoku.Solver.Coord
import sudoku.Solver.Value

object CERefSolver extends Solver[IO] {

  override def solve(givens: List[Value.Given]): IO[List[Solver.Value]] =
    for {
      allCells <- Coord.allCoords.traverse(Cell.make)
      givensMap = givens.map(g => g.coord -> g).toMap
      values   <- allCells.parTraverse(_.solve(givensMap, allCells))
    } yield values

  object Cell {
    def make(_coord: Coord): IO[Cell] = Deferred[IO, Value].map(_def =>
      new Cell {
        override val coord: Coord = _coord
        override protected[this] def deferredValue: Deferred[IO, Value] = _def
        override def deduceSingleCandidate(allCells: List[Cell]): IO[Value] =
          for {
            refCandidate    <- Ref.of[IO, Candidate](Candidate.initial(coord))
            peerCells = allCells.filter(_.coord.isPeerOf(coord))
            listOfSingleCandidateOrNever = peerCells.map(c => refineToSingleCandidateOrNever(refCandidate, c))
            singleCandidate <- raceMany(listOfSingleCandidateOrNever)
          } yield singleCandidate
        private def raceMany[A](as: List[IO[A]]): IO[A] = as.reduce((a1, a2) => (a1 race a2).map(_.merge))
        private def refineToSingleCandidateOrNever(refCandidate: Ref[IO, Candidate], peerCell: Cell): IO[Candidate.Single] =
          for {
            peerValue       <- peerCell.getValue
            singleCandidate <- refCandidate.modify {
                                 case multiple: Candidate.Multiple    =>
                                   multiple.refine(peerValue) match {
                                     case single: Candidate.Single     => (single, single.pure[IO])
                                     case multiple: Candidate.Multiple => (multiple, IO.never)
                                   }
                                 case alreadySingle: Candidate.Single => (alreadySingle, IO.never)
                               }.flatten
          } yield singleCandidate
      }
    )
  }

}
