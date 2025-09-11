package statistics

object Normalization {

  // (x - min) / (max - min)
  def normalizeMinMax(xs: Seq[Double]): Seq[Double] = {
    require(xs.nonEmpty, "Input sequence cannot be empty")
    val min = xs.min
    val max = xs.max
    val range = max - min
    if (range == 0) xs.map(_ => 0.0)
    else xs.map(x => (x - min) / range)
  }

  // (x - mean) / variance
  def normalizeByVariance(xs: Seq[Double]): Seq[Double] = {
    require(xs.nonEmpty, "Input sequence cannot be empty")
    val mean = xs.sum / xs.size
    val variance = xs.map(x => math.pow(x - mean, 2)).sum / xs.size
    xs.map(x => (x - mean) / variance)
  }

}
