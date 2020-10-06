package warmups

object MoreThanTwoViolations extends App {
  // DAY, MONTH, REASON, KM
  val data = List(
    (25, "February", "1", 25),
    (17, "April", "1", 63),
    (19, "March", "3", 45),
    (17, "October", "4", 12),
    (25, "December", "1", 45),
    (9, "January", "2", 56),
    (17, "April", "1", 54),
    (21, "September", "3", 16),
    (3, "May", "2", 59),
    (20, "January", "4", 46),
    (28, "June", "5", 28),
    (19, "March", "3", 34),
    (29, "August", "1", 42),
    (17, "April", "1", 77)
  )

  def contains[A](as: List[A], a: A): Boolean = as match {
    case Nil  => false
    case h::t => if (h==a) true else contains(t, a)
  }

  def distinct[A](tail: List[A], acc: List[A]): List[A] = tail match {
    case Nil => acc.reverse
    case h::t => if (contains(acc, h)) distinct(t, acc) else distinct(t, h::acc)
  }

  type T4 = (Int, String, String, Int)
  def moreThanTwoViolations(maxKm: Int, in: List[T4]) : List[(Int, String)] = {

    val cartesian: List[(T4, T4)] =
      in.flatMap { line1 =>
        in.map { line2 => (line1, line2) }
      }

    val filtered: List[(T4, T4)] = cartesian
        .filter { case ((d1,m1,r1,km1), (d2,m2,r2,km2)) =>
          d1==d2 && m1==m2 && r1==r2 && km1!=km2 && km1>maxKm && km2>maxKm
        }

    val mapped: List[(Int, String)] = filtered
        .map { case (a, _) => (a._1, a._2) }

    distinct(mapped, Nil)
  }

  println(moreThanTwoViolations(30, data))

}
