package ninetynine

object P26A {

  def tails[A](xs: List[A])(f: List[A] => List[List[A]]): List[List[A]] =
    xs match {
      case Nil    => List.empty
      case _ :: t => f(xs) ::: tails(t)(f)
    }

  def combinations[A](n: Int, as: List[A]): List[List[A]] =
    n match {
      case 0 => List(List.empty)
      case n =>
        tails(as) {
          case Nil    => sys.error("shouldn't be there by design")
          case h :: t => combinations(n - 1, t).map(h :: _)
        }
    }
}

object P26ARunner extends App {
  import P26A._

//  pprint.pprintln(tails("ABC".toList)(List(_)))
  pprint.pprintln(combinations(25, "ABCD".toList))
//  pprint.pprintln(combinations(3, "ABCDE".toList))
}
