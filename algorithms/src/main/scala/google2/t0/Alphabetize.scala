package google2.t0

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Alphabetize {

  def isToSort(c: Char) = c.isLower
  def isToSave(c: Char) = !isToSort(c)

  def alphabetizeV1(origin: String): String = {
    val L = origin.length

    val sorted: String = origin.filter(isToSort).sorted

    def reconstruct(outcome: List[Char], done: Int, used: Int): String = done match {
      case L                        => outcome.reverse.mkString
      case i if isToSave(origin(i)) => reconstruct(origin(i) :: outcome, i + 1, used)
      case i                        => reconstruct(sorted(used) :: outcome, i + 1, used + 1)
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
        case ((s2, buf), next) if isToSave(next) => (next :: s2)     -> buf
        case ((s2, buf), _)                      => (buf.head :: s2) -> buf.tail
      }
      ._1
      .reverse
      .mkString

  def alphabetizeV4(origin: String): String = {
    var sorted = origin.filter(isToSort).sorted
    val target = new Array[Char](origin.length)

    (0 until origin.length)
      .foreach { i =>
        val c: Char = origin(i)
        val z: Char =
          if (isToSave(c)) c
          else {
            val peek: Char = sorted.head
            sorted = sorted.substring(1)
            peek
          }
        target(i) = z
      }

    new String(target)
  }

  def alphabetizeV5(origin: String): String = {
    val sorted = origin.filter(isToSort).sorted

    def reconstruct(origin: List[Char], sorted: List[Char], acc: List[Char]): List[Char] = (origin, sorted) match {
      case (Nil, Nil)                   => acc
      case (Nil, s :: ss)               => reconstruct(Nil, ss, s :: acc)
      case (o :: os, Nil)               => reconstruct(os, Nil, o :: acc)
      case (o :: os, ss) if isToSave(o) => reconstruct(os, ss, o :: acc)
      case (_ :: os, s :: ss)           => reconstruct(os, ss, s :: acc)
    }

    reconstruct(origin.toList, sorted.toList, Nil).reverse.mkString
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
    } yield alphabetizeV5(in) shouldEqual out
  }

}
