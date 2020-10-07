package ninetynine

import tools.spec.ASpec
import scala.annotation.tailrec

/**
  * Extract a slice from a list
  * from I until K
  */
object P18R {
  
  def sliceAt(i: Int, k: Int, xs: List[Char]) = {

    def doSlice(c: Int, xs: List[Char]): List[Char] = xs match {
      case _ :: t if c < i =>      doSlice(c + 1, t)
      case h :: t if c < k => h :: doSlice(c + 1, t)
      case _               => Nil
    }

    doSlice(0, xs)
  }

}

object P18TR {

  def sliceAt(i: Int, k: Int, xs: List[Char]) = {

    @tailrec
    def doSlice(c: Int, tail: List[Char], acc: List[Char]): List[Char] = tail match {
      case _ :: t if c < i => doSlice(c + 1, t, acc)
      case h :: t if c < k => doSlice(c + 1, t, h :: acc)
      case _               => acc
    }

    doSlice(0, xs, Nil) reverse
  }

}

class P18RSpec extends ASpec {

  it("1") {
    val impls = Seq(
      P18R.sliceAt _,
      P18TR.sliceAt _,
    )
    
    val data = Seq(
      (3, 7, "") -> "",
      (0, 0, "abc") -> "",
      (0, 1, "abc") -> "a",
      (0, 2, "abc") -> "ab",
      (3, 7, "qwertyuiop") -> "rtyu",
      (3, 7, "qwer") -> "r",
    )

    for {
      impl <- impls
      ((from, to, in), out) <- data
    } impl(from, to, in.toList) shouldEqual out.toList

  }
}