package winitzki

/** the only idea is to represent */
object Disjunction {

  /** Example 1. Search
    *
    * we can return -1 if ot found
    * - but that makes things complicated
    */
  def findIndexBad(x: Int, xs: Array[Int]): Int = ???

  sealed trait SearchResult
  object SearchResult {
    case object NotFound           extends SearchResult
    case class FoundAt(index: Int) extends SearchResult
  }

  def findIndex(x: Int, xs: Array[Int]): SearchResult = ???

  /** Example 2. roots of square equation
    *
    * x^2^ + bx + c = 0
    *
    * we can return:
    * - empty array (if no solutions)
    * - array with one element (if 1 root)
    * - array with two elements (if 2 roots)
    * - but that makes things complicated
    */
  def solveBad(b: Double, c: Double): Array[Double] = ???

  sealed trait Solution {
    def nRoots: Int = this match {
      case _: Solution.TwoRoots => 2
      case _: Solution.OneRoot  => 1
      case _                    => 0
    }
    def hasRoots: Boolean = this match {
      case Solution.NoRoots => false
      case _                => true
    }
  }
  object Solution {
    case object NoRoots                         extends Solution
    case class OneRoot(x: Double)               extends Solution
    case class TwoRoots(x1: Double, x2: Double) extends Solution {
      override def equals(obj: Any): Boolean = obj match {
        case that: TwoRoots => (x1 == that.x1 && x2 == that.x2) || (x1 == that.x2 && x2 == that.x1)
        case _              => false
      }
    }
  }

  // https://en.wikipedia.org/wiki/Quadratic_equation
  def solve(a: Double, b: Double, c: Double): Solution =
    (b * b - 4 * a * c) match {
      case d if d > 0 =>
        val rd = math.sqrt(d)
        val `2a` = 2 * a
        Solution.TwoRoots((-b - rd) / `2a`, (-b + rd) / `2a`)
      case 0          => Solution.OneRoot(-b / (2 * a))
      case _          => Solution.NoRoots
    }

}

class Disjunction extends Base {

  import Disjunction.Solution._
  import Disjunction._

  test("solution") {
    solve(1, 1, 1) shouldBe NoRoots
    solve(1, 1, 0) shouldBe TwoRoots(0.0, -1.0)
    solve(1, 1, 0) shouldBe TwoRoots(-1.0, 0.0)
    solve(4, 4, 1) shouldBe OneRoot(-0.5)
  }

  test("has roots") {
    NoRoots.hasRoots shouldBe false
    TwoRoots(-1.0, 0.0).hasRoots shouldBe true
    OneRoot(-0.5).hasRoots shouldBe true
  }

  test("number of roots") {
    NoRoots.nRoots shouldBe 0
    OneRoot(-0.5).nRoots shouldBe 1
    TwoRoots(-1.0, 0.0).nRoots shouldBe 2
  }

}
