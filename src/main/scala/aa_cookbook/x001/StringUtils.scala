package aa_cookbook.x001

object StringUtils {
  implicit class SmartConverter1(val s: String) {
    def toInt_(radix: Int):Int = Integer.parseInt(s, radix)
  }
}
