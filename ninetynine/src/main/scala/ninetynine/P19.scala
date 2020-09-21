package ninetynine

object P19 {
  import P17TR.splitAt

  // n > 0 - rotate left
  def rotate(n: Int, xs: List[Symbol]): List[Symbol] =
    if (n > 0) splitAt(n, xs) match { case (l, r) => r ++ l }
    else if (n < 0) splitAt(xs.length + n, xs) match { case (l, r) => r ++ l }
    else xs

  def test(): Unit = {
    val source = List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k)
    println(s"Source: $source")
    val actual1 = rotate(3, source)  // List('d, 'e, 'f, 'g, 'h, 'i, 'j, 'k, 'a, 'b, 'c) <--
    val actual2 = rotate(-3, source) // List('i, 'j, 'k, 'a, 'b, 'c, 'd, 'e, 'f, 'g, 'h) -->
    println(s"Actual1: $actual1")
    println(s"Actual2: $actual2")
  }
  
}
