package ninetynine

import scala.annotation.tailrec

/**
  * Remove the Kth element from a list.
  */
object P20 {
  def extractAt[A](n: Int, xs: List[A]) = {

    @tailrec
    def doIt(cnt: Int, xs: List[A], acc: List[A]): (List[A], A) = xs match {
      case h :: t if cnt < n => doIt(cnt + 1, t, h :: acc)
      case h :: t            => (acc.reverse ::: t, h)
      case Nil => throw new NoSuchElementException
    }

    doIt(0, xs, Nil)
  }

}

class P20Spec extends NNSpec {
  import P20._
  
  it("1") {
    val data = Seq(
      (0, "AB") -> ("B", 'A'),
      (1, "AB") -> ("A", 'B'),
      (1, "ABCDE") -> ("ACDE", 'B'),
      (4, "ABCDE") -> ("ABCD", 'E'),
    )
    val ex = Seq(
      (0, ""),
      (5, "abcd"),
    )
    
    for {
      ((n, in), (out1, out2)) <- data
    } extractAt(n, in.toList) shouldEqual (out1.toList, out2)

    for {
      (n, in) <- ex
    } an[NoSuchElementException] should be thrownBy extractAt(n, in.toList)
    
    
  }
  
}
