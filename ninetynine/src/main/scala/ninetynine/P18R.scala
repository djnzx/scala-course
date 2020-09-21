package ninetynine

object P18R {
  
  def sliceAt(i: Int, k: Int, c: Int, xs: List[Symbol]): List[Symbol] = xs match {
    case Nil => ???
    case h :: t => if (c < i) sliceAt(i, k, c+1, t)
    else if (c < k) h::sliceAt(i, k, c+1, t)
    else Nil
  }

  def test(): Unit = {
    val source = List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k)
    println(s"Source: $source")
    val actual = sliceAt(3, 7, 0, source) //List('d, 'e, 'f, 'g)
    println(s"Actual: $actual")
  }
  
}
