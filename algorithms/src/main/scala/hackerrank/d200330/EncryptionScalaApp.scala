package hackerrank.d200330

import Console._

object EncryptionScalaApp extends App {

  def encryption(s0: String): String = {
    val s = s0.replace(" ", "")
    val len = s.length
    val l = math.sqrt(len)
    val l1 = math.floor(l).toInt
    val l2 = math.ceil(l).toInt
    val cols = l2
    val rows = if (l1 == l2) l2
      else if (l1 * l2 < len) l2
      else l1

    (for {
      c <- 0 until cols
      r <- 0 until rows
      idx = r * cols + c
      ch = if (idx < len) s(idx).toString else ""
      sep = if (r == rows-1 && c < cols-1) " " else ""
    } yield s"$ch$sep"
      ).mkString
  }

  val testData = Map(
    "chillout" -> "clu hlt io",
    "have a nice day" -> "hae and via ecy",
    "feed the dog" -> "fto ehg ee dd"
  )

  def message(r: Boolean): String = r match {
    case true  => s"${GREEN}OK$RESET"
    case false => s"${RED}ERR$RESET"
  }

  testData map { case (k,v) => ((k, v), encryption(k)) } foreach { case ((k,v), r) =>
    println(s".$k. => .$r. : ${message(v==r)}")
  }


}
