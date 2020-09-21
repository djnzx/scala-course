package ninetynine

import scala.annotation.tailrec

object P12TR {
  
  def unpackOne(sym: Symbol, cnt: Int): List[Symbol] =
    1 to cnt map { _ => sym } toList

  @tailrec
  def unpack(xs: List[Any], acc: List[Symbol]): List[Symbol] = xs match {
    case Nil => acc
    case h :: t => h match {
      case sym: Symbol             => unpack(t, acc :+ sym)
      case (sym: Symbol, cnt: Int) => unpack(t, acc ++ unpackOne(sym, cnt))
      case _                       => ???
    }
  }

  def test(): Unit = {
    val source: List[Any] = List('x, ('a,4), 'b, ('c,2), ('a,2), 'd, ('e,4))
    val expected = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
    println(s"Source:   $source")
    val actual = unpack(source, Nil)
    println(s"Expected: $expected")
    println(s"Actual:   $actual")
  }
  
}
