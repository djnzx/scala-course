package ninetynine

object P15R {
  
  def nTimes(n: Int, sym: Symbol): List[Symbol] = 1 to n map { _ => sym } toList

  def duplicate(n: Int, xs: List[Symbol]): List[Symbol] = xs match {
    case Nil    => Nil
    case h :: t => nTimes(n, h) ++ duplicate(n, t)
  }

  def test(): Unit = {
    val source = List('x, 'a, 'b, 'c, 'a, 'd, 'e)
    println(s"Source: $source")
    val actual = duplicate(3, source)
    println(s"Actual: $actual")
  }
  
}
