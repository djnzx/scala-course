package ninetynine

object P26A {

  def tails[A](xs: List[A])(f: List[A] => List[List[A]]): List[List[A]] =
    xs match {
      case Nil    => List.empty
      case _ :: t => f(xs) ::: tails(t)(f)
    }

  def combinations[A](n: Int, as: List[A]): List[List[A]] = n match {
    case 0 => List(List.empty)
    case n =>
      tails(as) {
        case h :: t => combinations(n - 1, t).map(h :: _)
        case Nil    => sys.error("not supposed to be here")
      }
  }

}

object P26ARunner extends App {
  import P26A._

//  pprint.pprintln(tails("ABC".toList)(List(_)))
  pprint.pprintln(combinations(2, "ABCD".toList))
//  pprint.pprintln(combinations(3, "ABCDE".toList))
}
