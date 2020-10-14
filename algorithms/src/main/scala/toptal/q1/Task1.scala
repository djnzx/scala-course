package toptal.q1

object Task1 {

  def solution(s: String, x: Array[Int], y: Array[Int]): Int =
    (s zip (x zip y).map { case (x, y) => x * x + y * y })
      .groupMap { case (_, r) => r } { case (ch, _) => ch }
      .toVector
      .sortBy { case (r, _) => r }
      .view
      .map { case (_, cs) => cs }
      .takeWhile { cs => cs.size == cs.toSet.size }
      .foldLeft((false, Set.empty[Char])) {
        case ((done, set), chars) =>
          if (!done && chars.forall(!set.contains(_))) {
            (false, set ++ chars)
          } else {
            (true, set)
          }
      }
    match { case (_, set) => set.size }
}
