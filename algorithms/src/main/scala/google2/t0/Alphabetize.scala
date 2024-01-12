package google2.t0

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Alphabetize {

  def isToSort(c: Char) = c.isLower
  def isToSave(c: Char) = !isToSort(c)

  def alphabetizeV1(origin: String): String = {
    val L = origin.length

    val sorted: String      = origin.filter(isToSort).sorted
    val toSaveInd: Set[Int] = origin.indices.filter(i => isToSave(origin(i))).toSet

    def reconstruct(outcome: List[Char], done: Int, used: Int): String = done match {
      case L                 => outcome.reverse.mkString
      case i if toSaveInd(i) => reconstruct(origin(i) :: outcome, i + 1, used)
      case i                 => reconstruct(sorted(used) :: outcome, i + 1, used + 1)
    }

    reconstruct(Nil, 0, 0)
  }

  def alphabetizeV2(origin: String): String = {
    val indexed = origin.zipWithIndex
    val saved   = indexed.filter { case (c, _) => isToSave(c) }
    val toSort  = indexed.filter { case (c, _) => isToSort(c) }
    val sorted  = toSort.map(_._1).sorted zip toSort.map(_._2).sorted
    (saved ++ sorted).sortBy(_._2).map(_._1).mkString
  }

  def alphabetizeV3(origin: String): String =
    origin
      .foldLeft(
        List.empty[Char] -> origin.filter(isToSort).sorted.toList
      ) {
        case ((s2, buf), next) if isToSave(next) => (next :: s2) -> buf
        case ((s2, next :: rest), _)             => (next :: s2) -> rest
        case ((_, Nil), _)                       => ???
      }
      ._1
      .reverse
      .mkString

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
    } yield alphabetizeV3(in) shouldEqual out
  }

}
