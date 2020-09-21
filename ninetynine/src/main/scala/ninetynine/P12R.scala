package ninetynine

object P12R {
  
  def unpackOne(sym: Symbol, cnt: Int): List[Symbol] =
    1 to cnt map { _ => sym } toList

  def unpack(xs: List[Any]): List[Symbol] = xs match {
    case Nil => Nil
    case h :: t => h match {
      case sym: Symbol             => sym :: unpack(t)
      case (sym: Symbol, cnt: Int) => unpackOne(sym, cnt) ++ unpack(t)
      case _                       => ???
    }
  }

  def test(): Unit = {
    val source: List[Any] = List('x, ('a,4), 'b, ('c,2), ('a,2), 'd, ('e,4))
    val expected = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
    println(s"Source:   $source")
    val actual = unpack(source)
    println(s"Expected: $expected")
    println(s"Actual:   $actual")
  }
  
}
