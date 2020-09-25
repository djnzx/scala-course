package ninetynine

import scala.annotation.tailrec

/**
  * Duplicate the elements of a list
  */
object P14TR {
  
  @tailrec
  private def duplicate(xs: List[Char], acc: List[Char]): List[Char] = xs match {
    case Nil    => acc
    case h :: t => duplicate(t, h :: h :: acc)
  }

  def duplicate(xs: List[Char]): List[Char] = duplicate(xs, Nil) reverse

}

class P14TRSpec extends NNSpec {
  import P14TR._

  it("1") {
    Vector(
      "" -> "",
      "A" -> "AA",
      "AB" -> "AABB",
    )
      .foreach { case (in, out) =>
        duplicate(in.toList).mkString shouldEqual out
      }
  }
}
