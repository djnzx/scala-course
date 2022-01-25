package google2.t0

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Alphabetize {

  def alphabetize(origin: String): String = {
    val L = origin.length
    val toSort = (c: Char) => c.isLower
    val toSave = (c: Char) => !toSort(c)

    val sorted: String = origin.filter(toSort).sorted
    val saved: Set[Int] = origin.indices.filter(i => toSave(origin(i))).toSet

    def reconstruct(outcome: List[Char], constructed: Int, used: Int): String = constructed match {
      case `L`                    => outcome.reverse.mkString
      case i if saved.contains(i) => reconstruct(origin(i) :: outcome, i + 1, used)
      case i                      => reconstruct(sorted(used) :: outcome, i + 1, used + 1)
    }

    reconstruct(Nil, 0, 0)
  }

}

class AlphabetizeSpec extends AnyFunSpec with Matchers {
  import Alphabetize._

  it("alphabetize") {
    val io = Map(
      "Google Mail"           -> "Gaegil Mloo",
      "GooZgleX Mail"         -> "GaeZgilX Mloo",
      "Don't worry, Be Happy" -> "Dae'n ooppr, Br Htwyy",
    )

    for {
      (in, out) <- io
    } yield alphabetize(in) shouldEqual out
  }

}
