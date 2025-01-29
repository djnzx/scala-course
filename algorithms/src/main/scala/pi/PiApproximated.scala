package pi

import scala.util.Random

/*
       S square   4
       -------- = --
       S circle   Pi
 */
object PiApproximated extends App {

  def next = Random.nextDouble() * 2 - 1

  val SAMPLES = 1_000_000

  val (square, circle) = (1 to SAMPLES)
    .foldLeft((0L, 0L)) { case ((square, circle), _) =>
      val x = next
      val y = next
      val d = math.sqrt(x * x + y * y)
      val isCircle = if (d <= 1.0) 1L else 0L
      (square + 1, circle + isCircle)
    }

  val apprxPi = 4.toDouble * circle / square
  pprint.log(math.Pi)
  pprint.log(apprxPi)

}
