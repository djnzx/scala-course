package cookbook.x009

object P0intro2 extends App {
  val s = "1"
  val i = try {
    s.toInt
  } catch {
    case _: Throwable => 0
  }
}
