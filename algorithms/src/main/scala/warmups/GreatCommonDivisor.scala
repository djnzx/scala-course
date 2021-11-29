package warmups

import scala.annotation.tailrec

object GreatCommonDivisor extends App {

  @tailrec
  def gcd(a: Int, b: Int): Int =
    if (b == 0) a
    else gcd(b, a % b)

  val dataSet = List(
    (24, 16)  -> 8,
    (100, 10) -> 10,
    (120, 80) -> 40,
    (80, 120) -> 40,
    (100, 20) -> 20,
    (37, 11)  -> 1,
    (120, 90) -> 30,
  )

  dataSet.foreach(d => assert(d._2 == gcd(d._1._1, d._1._2)))
}
