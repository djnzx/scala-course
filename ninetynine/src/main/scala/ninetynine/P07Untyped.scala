package ninetynine

object P07Untyped {
  
  def flatten(xs: List[Any]): List[Any] = xs match {
    case Nil => Nil
    case h::t => h match {
      case n: Int       => n :: flatten(t)
      case l: List[Any] => flatten(l) ++ flatten(t)
    }
  }

  def test(): Unit = {
    val data = List(List(1, 1), 2, List(3, List(5, List(8))))
    val r = flatten(data)
    println(data)
    println(r)
  }
  
}
