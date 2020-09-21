package ninetynine

object P26 {
  /**
    * a ++ b  === concatenation for any iterables
    * a ::: b === concatenation for lists especially
    *
    * that's variance of flatMap
    * which joins results f(list) and flatMapTail(list.tail)(f)
    *
    * if we provide identity function as a parameter
    * flatMapTail(List('a, 'b, 'c, 'd))(l => l) will produce:
    *
    * List('a, 'b, 'c, 'd,   'b, 'c, 'd,   'c, 'd,   'd)
    */
  def tail[A, B](la: List[A])(f: List[A] => List[B]): List[B] = la match {
    case Nil => Nil
    case _ :: t => f(la) ::: tail(t) {
      f
    }
  }

  def combinations[A](n: Int, la: List[A]): List[List[A]] = n match {
    case 0 => List(Nil)
    case _ => tail(la) { case h :: t => combinations(n - 1, t) map { x => h :: x } }
  }

  def test(): Unit = {
    val data: List[Symbol] = List('a, 'b, 'c, 'd, 'e)
    println(data)
    val r = combinations(3, data) // List(List('a, 'b, 'c), List('a, 'b, 'd), List('a, 'c, 'd), List('b, 'c, 'd))
    println(r)
  }
  
}