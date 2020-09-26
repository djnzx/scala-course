package ninetynine

/**
  * Rotate a list N places to the left
  * n > 0 - rotate right
  * n < 0 - rotate left
  * n == 0 - do not rotate
  */
object P19 {
  import P17.splitAt

  def rotate(n: Int, xs: List[Char]): List[Char] = n match {
    case 0 => xs
    case _ =>
      val l = xs.length
      val nl = n % l
      //            right             left
      val at = if (nl > 0) l - nl else -nl
      val (lp, rp) = splitAt(at, xs)
      rp ::: lp
  }

}

class P19Spec extends NNSpec {
  import P19._
  
  it("1") {
    val data = Seq(
      ( 0, "abc") -> "abc",
      ( 1, "abc") -> "cab",
      (-1, "abc") -> "bca",
      ( 4, "abc") -> "cab",
      ( 1000000000, "abc") -> "cab",
      (-4, "abc") -> "bca",
      ( 3, "abcdefghi") -> "ghiabcdef",
      (-3, "abcdefghi") -> "defghiabc",
    )
    for {
      ((n, in), out) <- data
    } rotate(n, in.toList).mkString shouldEqual out
  }
  
}