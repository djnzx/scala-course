package ninetynine

object P14R {
  
  def duplicate(xs: List[Symbol]): List[Symbol] = xs match {
    case Nil    => Nil
    case h :: t => h :: h :: duplicate(t)
  }

  def test(): Unit = {
    val source = List('x, 'a, 'b, 'c, 'a, 'd, 'e)
    println(s"Source: $source")
    val actual = duplicate(source)
    println(s"Actual: $actual")
  }
  
}
