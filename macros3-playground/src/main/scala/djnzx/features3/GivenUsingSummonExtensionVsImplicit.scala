package djnzx.features3

object GivenUsingSummonExtensionVsImplicit extends App {

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

  /** Scala 2 */
  implicit class IntSyntax(a: Int) {
    def print2(s: String) = (1 to a).foreach(_ => println(s))
  }
  /** Scala 3 */
  extension (a: Int)
    def print3(s: String) = (1 to a).foreach(_ => println(s))

  2.print2("Hello!")
  3.print3("Hi, there!")
}
