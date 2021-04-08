package rock

object GivenUsingSummonVsImplicits extends App {

  /** Scala 2 */
  def add2(a: Int)(implicit b: Int): Int = a + b
  /** Scala 3 */
  def add3(a: Int)(using b: Int): Int = a + b

  /** Scala 2 */
  implicit val b1: Int = 3
  /** Scala 3 */
  given b2: Double = 42.13

  /** Scala 2 */
  val i1 = implicitly[Int]
  /** Scala 3 */
  val i2 = summon[Double]

}
