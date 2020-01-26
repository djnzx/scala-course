package aa_cookbook.x002

object Big extends App {
  val z1 = BigInt(1234567890)
  val z2 = BigInt("123456789000000000000000")
  val z3 = z1 * z2
  println(z3.isValidInt)
  println(z3.isValidLong)
  println(z3)

  val x1: Double = Double.MinValue
  val x2: Double = Double.NegativeInfinity

  println(x1)
  println(x2)
  println(Double.NegativeInfinity + Double.PositiveInfinity)
}
