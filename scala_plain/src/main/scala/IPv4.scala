import scala.util.Try

object IPv4 {
  val SIZE = 4
  case class IP(a: Int, b: Int, c: Int, d: Int)
  
  def validate(s: String): Option[IP] = for {
    s1 <- Option(s)
    s2 = s1.split(".")
    if s2.length == SIZE
    s3 = s2.flatMap(s => Try(s.toInt).toOption)
    if s3.length == SIZE
    i4 = s3.flatMap(x => Option.when(x >= 0 && x <= 255)(x))
    if i4.length == SIZE
  } yield IP(i4(0), i4(1), i4(2), i4(3))
  
}
