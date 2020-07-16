package toptal

object Task1 {

  def solution(s: String, x: Array[Int], y: Array[Int]): Int =
    (s zip (x zip y).map { case (x, y) => x * x + y * y })
      .sortBy(_._2)
      // and right here we need collect just to first duplicate letter
      .groupBy(_._2)
      .toVector
      .map { case (r, sq) => (r, sq.map(_._1)) }
      .sortBy(_._1)
      .takeWhile { case (_, sq) => sq.length == 1 }
      .foldLeft((0, Map.empty[Char, Long])) { case ((mx, map), (_, sqc)) =>
        val map2: Map[Char, Long] = sqc.foldLeft(map) { (m, l) => m.updated(l, m.getOrElse(l, 0L) + 1L) }
        val max2 = mx max map2.count { case (_, v) => v == 1 }
        (max2, map2)
      }
      ._1
  
  
  
}
