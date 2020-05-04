package ninetynine

object NN26App extends App {

  def tail[A, B](la: List[A])(f: List[A] => List[B]): List[B] = la match {
    case Nil  => Nil
    case _::t => f(la) ::: tail(t) { f }
    //          List[B]   List[B]
  }

  println(
    tail(List(1,2,3,4,5)) { _.map(_ + 100) }
  )

}
