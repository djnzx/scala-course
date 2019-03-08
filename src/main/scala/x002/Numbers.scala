package x002

import x001.StringUtils._

object Numbers extends App {
  implicit class SmartConverter2(s: String) {
    def toInt(radix: Int):Int = Integer.parseInt(s, radix)
  }
  println(Char.MaxValue)
  println(Byte.MaxValue)
  println(Short.MaxValue)
  println(Int.MaxValue)
  println(Long.MaxValue)
  println(Float.MaxValue)
  println(Double.MaxValue)
  println(Boolean)
  val b1 = BigInt(1)
  val b2 = BigDecimal(1)
  println(b1.bigInteger)
  println(b2)

  val z1 = Integer.parseInt("10", 2)
  println(z1) //2
  val z2 = "10".toInt(2)
  println(z2)
  val z3 = "10".toInt_(2)
}
