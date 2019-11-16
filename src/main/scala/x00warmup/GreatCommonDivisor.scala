package x00warmup

object GreatCommonDivisor extends App {

  /**
    * prerequisite: a must be bigger than b
    */
  def gcd(a: Int, b:Int): Int = {
    val r: Int = a % b
    if (r == 0) b else gcd(b, r)
  }

  val dataSet = List(
    (24,16) -> 8,
    (100,10) -> 10,
    (100,20) -> 20,
    (37,11) -> 1,
    (120,90) -> 30,
  )

  dataSet.foreach(d => assert(d._2 == gcd(d._1._1, d._1._2)))
}
