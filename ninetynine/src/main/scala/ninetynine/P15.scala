package ninetynine

import scala.annotation.tailrec

/**
  * Duplicate the elements of a list a given number of times
  */
object P15 {
  def timesN(n: Int, c: Char): List[Char] = List.fill(n)(c)
}


object P15R {
  import P15._

  def duplicateN(n: Int, xs: List[Char]): List[Char] = xs match {
    case Nil    => Nil
    case h :: t => timesN(n, h) ++ duplicateN(n, t)
  }

}

object P15TR {
  import P15._
  
  @tailrec
  private def duplicateN(n: Int, xs: List[Char], acc: List[Char]): List[Char] = xs match {
    case Nil    => acc
    case h :: t => duplicateN(n, t, acc ++ timesN(n, h))
  }

  def duplicateN(n: Int, xs: List[Char]): List[Char] = duplicateN(n, xs, Nil)
}

class P15RSpec extends NNSpec {

  it("1") {
    val impls = Seq(
      P15R.duplicateN _,
      P15TR.duplicateN _,
    )

    val data = Seq(
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
    
    for {
      impl <- impls
      ((n, in), out) <- data
    } impl(n, in.toList).mkString shouldEqual out
    
  }
}
