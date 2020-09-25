package ninetynine

import scala.annotation.tailrec

/**
  * Decode a run-length encoded list
  */
object P12ATR {
  
  def unpackOne(c: Char, n: Int): List[Char] = List.fill(n)(c)

  @tailrec
  private def unpack(xs: List[(Int, Char)], acc: List[Char]): List[Char] = xs match {
    case Nil         => acc
    case (n, c) :: t => unpack(t, unpackOne(c, n) ::: acc)
  }
  
  def unpack(xs: List[(Int, Char)]): List[Char] = unpack(xs, Nil) reverse

}

class P12ATRSpec extends NNSpec {
  import P12ATR._
  
  it("1") {
    Vector(
      List() -> "",
      List((1,'A')) -> "A",
      List((2,'A')) -> "AA",
      List((2,'A'), (1,'B')) -> "AAB",
      List((2,'A'), (2,'B')) -> "AABB",
    )
      .foreach { case (in, out) =>
        unpack(in).mkString shouldEqual out
      }
  }
  
} 
