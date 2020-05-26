package fp_red.red01_05

object Red01 extends App {

  def dropWhile[A](xs: List[A], f: A => Boolean): List[A] = xs match {
    case Nil => Nil
    case h::t => if (f(h)) dropWhile(t, f) else t
  }

  val r = dropWhile[Int](List(1,2,3,4,5,6), x => x < 3)
}
