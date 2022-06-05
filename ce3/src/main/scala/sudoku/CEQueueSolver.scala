package sudoku

import cats.effect.IO
import cats.effect.std.Queue
import cats.implicits._
import sudoku.Solver.Candidate
import sudoku.Solver.Coord
import sudoku.Solver.Value

object CEQueueSolver extends Solver[IO] {
  override def solve(givenValues: List[Value.Given]): IO[List[Value]] =
    for {
      givenCoords   <- IO(givenValues.map(_.coord).toSet)
      missingCoords = Coord.allCoords.filterNot(givenCoords.contains)
      missingCells  <- missingCoords.traverse(MissingCell.make)
      broadcast = broadcastToPeers(missingCells)(_)
      _             <- givenValues.parTraverse_(broadcast)
      missingValues <- missingCells.parTraverse(cell => cell.solve.flatTap(broadcast))
    } yield givenValues ++ missingValues

  case class MissingCell(coord: Coord, updatesQueue: Queue[IO, Value]) {
    val solve: IO[Candidate.Single] = refineToSingleCandidate(Candidate.initial(coord))

    private def refineToSingleCandidate(candidates: Candidate.Multiple): IO[Candidate.Single] =
      for {
        peerValue       <- updatesQueue.take
        singleCandidate <- candidates.refine(peerValue) match {
                             case single: Candidate.Single     => IO.pure(single)
                             case multiple: Candidate.Multiple => refineToSingleCandidate(multiple)
                           }
      } yield singleCandidate
  }

  object MissingCell {
    def make(coord: Coord): IO[MissingCell] =
      Queue.unbounded[IO, Value].map(queue => MissingCell(coord, queue))
  }

  def broadcastToPeers(cells: List[MissingCell])(update: Value): IO[Unit] =
    cells
      .filter(cell => cell.coord.isPeerOf(update.coord))
      .parTraverse_(cell => cell.updatesQueue.offer(update))
}
