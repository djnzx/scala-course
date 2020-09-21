package ninetynine

import scala.annotation.tailrec

class P07TROneLevel {
  
  def flatten(xsa: List[Any]): List[Any] = {
    @tailrec
    def flatten(xs: List[Any], acc: List[Any]): List[Any] = xs match {
      case Nil => acc
      case h::t => h match {
        case n: Int       => flatten(t, acc :+ n)
        case l: List[Any] => flatten(t, acc ++ l)
      }
    }
    flatten(xsa, Nil)
  }

  def test(): Unit = {
    val data: List[Any] = List(List(1, 1), 2, List(3, List(5, 8)))
    val r = flatten(data)
    println(data)
    println(r)
  }

}
