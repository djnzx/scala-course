package x002

import x001.StringUtils._

object Numbers extends App {

  implicit class SmartConverter2(s: String) {
    def toInt2(radix: Int):Int = Integer.parseInt(s, radix)
  }

  /**
    * for java interop
    * @throws java.lang.NumberFormatException
    * @return
    */
  @throws(classOf[NumberFormatException])
  def toIntBreakable = (s: String) => Integer.parseInt(s)

  def toIntOption(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case e: NumberFormatException => None
    }
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
  val z2 = "10".toInt2(2)
  println(z2)
  val z3 = "10".toInt_(2)
  println(toIntBreakable("99"))
//  println(toIntBreakable("99 ")) // will break

  println(toIntOption("88").getOrElse(-1))
  println(toIntOption("fg").getOrElse(-1))
  val z4 = toIntOption("88").getOrElse(-1)

  toIntOption("55") match {
    case Some(n) => println(n)
    case None => println("NaN")
  }
  toIntOption("55 ") match {
    case Some(n) => println(n)
    case None => println("NaN")
  }
  val z5 = toIntOption("55 ") match {
    case Some(n) => n
    case None => -1
  }
  println(z5)

}
