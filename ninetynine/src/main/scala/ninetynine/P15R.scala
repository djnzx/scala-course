package ninetynine

/**
  * Duplicate the elements of a list a given number of times
  */
object P15R {
  
  def timesN(n: Int, c: Char): List[Char] = List.fill(n)(c)

  def duplicateN(n: Int, xs: List[Char]): List[Char] = xs match {
    case Nil    => Nil
    case h :: t => timesN(n, h) ++ duplicateN(n, t)
  }

}

class P15RSpec extends NNSpec {
  import P15R._

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
