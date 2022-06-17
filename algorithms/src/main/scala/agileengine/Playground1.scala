package agileengine

object Playground1 extends App {

  def solution(x: Int, y: Int, aa: Array[Int]): Int =
    aa.foldLeft((0, 0, 0, -1)) { case ((idx, xc, yc, r), a) =>
      val xc2 = xc + (if (x == a) 1 else 0)
      val yc2 = yc + (if (y == a) 1 else 0)
      val r2 = if (xc2 == yc2) idx else r
      (idx + 1, xc2, yc2, r2)
    }._4

}
