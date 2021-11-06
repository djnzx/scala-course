package topics.pattern

object PMList extends App {

  def ma[A](xs: List[A]) = xs match {
    case Nil => "empty"
//    case a :: t => s"h:$a, t:$t"
    case i :+ l => s"i:$i, l:$l"
  }

  println(ma(List()))

}
