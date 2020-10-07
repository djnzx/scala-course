package ninetynine

import tools.spec.ASpec
import scala.annotation.tailrec

/**
  * Decode a run-length encoded list
  */
object P12BTR {
  def unpackOne(c: Char, n: Int): List[Char] = List.fill(n)(c)

  @tailrec
  private def unpack(xs: List[Any], acc: List[Char]): List[Char] = xs match {
    case (c: Char) :: t         => unpack(t, unpackOne(c, 1) ::: acc)
    case (n: Int, c: Char) :: t => unpack(t, unpackOne(c, n) ::: acc)
    case _                      => acc
  }

  def unpack(xs: List[Any]): List[Char] = unpack(xs, Nil) reverse

}

class P12BTRSpec extends ASpec {
  import P12BTR._
  
  it("1") {
    Vector(
      List() -> "",
      List('A') -> "A",
      List((2,'A')) -> "AA",
      List((2,'A'), 'B') -> "AAB",
      List((2,'A'), (2,'B')) -> "AABB",
      List((2,'A'), (2,'B'), 'C') -> "AABBC",
      List((2,'A'), (2,'B'), 'C', (3, 'D')) -> "AABBCDDD",
    )
      .foreach { case (in, out) =>
        unpack(in).mkString shouldEqual out
      }
  }
  
} 
