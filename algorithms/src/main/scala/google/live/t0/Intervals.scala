package google.live.t0

object Intervals extends App {
  val src = List((1, 7), (5, 9))
  val points = src.flatMap { case (a, b) => List(a, b) }.toSet.toVector.sorted
  val indexes = points.indices.drop(1)
  val pairs = indexes.map(i => (points(i - 1), points(i)))
  val pairsAndCount = pairs.map { case p @ (a, b) =>
    p -> src.count { case (x, y) =>
      val m = (a.toDouble + b) / 2
      (m >= x) && (m <= y)
    }
  }
  pprint.pprintln(pairsAndCount)
}
