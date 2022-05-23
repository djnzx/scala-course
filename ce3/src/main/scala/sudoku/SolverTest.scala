package sudoku

import cats.effect.IO
import munit.CatsEffectSuite
import sudoku.Solver.Value
import sudoku.TestData._

class SolverTest extends CatsEffectSuite {

  val testCases = Seq(testCase1, testCase2, testCase3)

  val solvers = List(
    CERefSolver
//      CatsEffectQueueSolver,
//      FS2StreamSolver
  )

  solvers.foreach { solver =>
    testCases.foreach { givensAndSolved =>
      test(s"${solver.getClass.getSimpleName.stripSuffix("$")} should solve sudoku puzzle") {

        val givens = givensAndSolved.collect { case (k, SudokuValue.Given(v)) => Value.Given(k, v) }.toList

        val result = solver
          .solve(givens)
          .flatTap(vs => IO(println(Solver.toString(vs))))

        assertIO(
          result.map(_.map(toSudokuValue).toMap),
          givensAndSolved
        )
      }
    }
  }

}
