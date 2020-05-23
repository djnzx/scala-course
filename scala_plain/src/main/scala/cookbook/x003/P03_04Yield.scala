package cookbook.x003

import scala.util.Random

object P03_04Yield extends App {
  val r = Random
  var ra = for (i <- 1 to 50) yield r.nextInt(20)
  val ra2 = for {
    i <- ra.indices
    if ra(i) < 10
  } yield ra(i)
  println(ra.mkString(","))
  println(ra2.mkString(","))
  println(ra2.distinct.mkString(","))
  println(ra2.distinct.sorted.mkString(","))
}
