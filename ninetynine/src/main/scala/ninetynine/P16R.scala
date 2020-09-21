package ninetynine

object P16R {
  
  def dropNth(n: Int, xs: List[Symbol]): List[Symbol] = {

    def dropNth(n: Int, cnt: Int, xs: List[Symbol]): List[Symbol] = xs match {
      case Nil    => Nil
      case h :: t =>
        if (n==cnt) dropNth(n, 1, t)
        else        h::dropNth(n, cnt+1, t)
    }

    dropNth(n, 1, xs)
  }

  def test(): Unit = {
    val source = List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k)
    println(s"Source: $source")
    val actual = dropNth(3, source)
    println(s"Actual: $actual")
  }
  
}
