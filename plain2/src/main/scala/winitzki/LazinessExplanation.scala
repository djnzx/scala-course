package winitzki

object LazinessExplanation {

  /** computed EVERY time is called, not cached */
  def f(x: Int): Int = x + 1

  /** eager, computed IMMEDIATELY on EVERY call */
  val a = f(1)

  /** lazy, computed ONCE on FIRST call and cache */
  lazy val b = f(1)

  /** never, can't be computed due to the exception */
  val e: Nothing = throw new IllegalArgumentException()

}
