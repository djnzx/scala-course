package cry

object Utils {

  private val digits = "0123456789abcdef"

  def byteToHex(b: Byte): String = {
    val d2 = digits(b & 15)
    val d1 = digits((b >>> 4) & 15)
    new String(Array(d1, d2))
  }

  def bytesToHex(bs: Array[Byte]): String = bs.map(byteToHex).mkString

}
