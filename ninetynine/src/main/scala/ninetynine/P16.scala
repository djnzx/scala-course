package ninetynine

import scala.annotation.tailrec

/**
  * Drop every Nth element from a list
  */
object P16R {
  
  def dropNth(n: Int, xs: List[Char]) = {

    def dropNth(cnt: Int, xs: List[Char]): List[Char] = xs match {
      case Nil                => Nil
      case _ :: t if n == cnt => dropNth(1, t)
      case h :: t             => h :: dropNth(cnt + 1, t)
    }

    dropNth(1, xs)
  }

}

object P16TR {

  def dropNth(n: Int, xs: List[Char]) = {

    @tailrec
    def dropNth(cnt: Int, xs: List[Char], acc: List[Char]): List[Char] = (xs, cnt) match {
      case (Nil, _)    => acc
      case (_ :: t, 1) => dropNth(n,     t,      acc)
      case (h :: t, c) => dropNth(c - 1, t, h :: acc)
    }

    dropNth(n, xs, Nil) reverse
  }

}

class P16RSpec extends NNSpec {
  
  it("1") {
    val impls = Seq(
      P16R.dropNth _,
      P16TR.dropNth _,
    )
    
    val data = Vector(
      (1, "") -> "",
      (1, "abc") -> "",
      (2, "abcdef") -> "ace",
      (3, "abcdefghijk") -> "abdeghjk",
    )
    
    for {
      impl <- impls
      ((n, in), out) <- data
    } impl(n, in.toList).mkString shouldEqual out
    
  }
}