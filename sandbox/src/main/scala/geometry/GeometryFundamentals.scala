package geometry

trait GeometryFundamentals {

  def sq(x: Int): Int = x * x

  def distance(d1: Int, d2: Int, d3: Int): Double = math.sqrt(sq(d1) + sq(d2) + sq(d3))

  def isNat(x: Double): Option[Int] = Option(x.toInt).filter(_.toDouble == x)

  def distanceIsNat(d1: Int, d2: Int, d3: Int): Option[Int] = isNat(distance(d1, d2, d3))

}

object GeometryFundamentals extends GeometryFundamentals
