package ninetynine

import scala.annotation.tailrec

/**
  * Duplicate the elements of a list a given number of times
  */
object P15TR {

  def timesN(n: Int, c: Char): List[Char] = List.fill(n)(c)

  @tailrec
  def duplicateN(n: Int, xs: List[Char], acc: List[Char] = Nil): List[Char] = xs match {
    case Nil    => acc
    case h :: t => duplicateN(n, t, acc ++ timesN(n, h))
  }

}

class P15TRSpec extends NNSpec {
  import P15TR._

  it("1") {
    Vector(
      (1, "") -> "",
      (1, "A") -> "A",
      (1, "AB") -> "AB",
      (2, "") -> "",
      (2, "A") -> "AA",
      (2, "AB") -> "AABB",
      (3, "") -> "",
      (3, "A") -> "AAA",
      (3, "AB") -> "AAABBB",
    )
      .foreach { case ((n, in), out) =>
        duplicateN(n, in.toList).mkString shouldEqual out
      }
  }
}
