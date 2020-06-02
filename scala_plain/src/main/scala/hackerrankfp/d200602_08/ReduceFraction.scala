package hackerrankfp.d200602_08

object ReduceFraction extends App {
  
  /**
    * great common divisor
    */
  def gcd(a: Int, b:Int): Int = {
    val r = a % b
    if (r == 0) b else gcd(b, r)
  }

  /**
    * Fractional representation
    */
  case class Frac(nom: Int, den: Int) {
    def reduce: Boolean = gcd(nom, den) > 1
    def reduced: Frac = gcd(nom, den) match {
      case 1 => this
      case n => Frac(nom/n, den/n)
    }
    override def toString: String = s"$nom/$den"
  }
  
  Map(
    Frac(3,  4)  -> Frac(3, 4),
    Frac(6,  4)  -> Frac(3, 2),
    Frac(12, 24) -> Frac(1, 2)
  ).foreach { case (k,v) => assert(k.reduced == v) }
  
}
