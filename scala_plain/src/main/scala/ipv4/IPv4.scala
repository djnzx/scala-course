package ipv4

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Try

object IPv4 {
  type IPS = Map[Int, Map[Int, Map[Int, Set[Int]]]]
  val empty: IPS = Map.empty
  val SIZE = 4
  case class IP(a: Int, b: Int, c: Int, d: Int)
  def map1(d: Int) = Set(d)
  def map2(c: Int, d: Int) = Map(c -> map1(d))
  def map3(b: Int, c: Int, d: Int) = Map(b -> map2(c, d))
  def map4(a: Int, b: Int, c: Int, d: Int) = Map(a -> map3(b, c, d))
  implicit class IPSyntax(private val ip: IP) extends AnyVal {
    def toMap = map4(ip.a, ip.b, ip.c, ip.d)
  }
  
  def stringToIP(s: String): Option[IP] = for {
    s1 <- Option(s)
    s2 = s1.split(".")
    if s2.length == SIZE
    s3 = s2.flatMap(s => Try(s.toInt).toOption)
    if s3.length == SIZE
    i4 = s3.flatMap(x => Option.when(x >= 0 && x <= 255)(x))
    if i4.length == SIZE
  } yield IP(i4(0), i4(1), i4(2), i4(3))

  def len(a: Array[_]) = a.length == SIZE
  def stringToIP2(s: String): Option[IP] = Option(s)
    .map(_.split("."))
    .filter(len)
    .map(_.flatMap(x => Try(x.toInt).toOption))
    .filter(len)
    .map(_.filter(x => x >=0 && x <= 255))
    .filter(len)
    .map { case Array(a, b, c, d) => IP(a, b, c, d) }
  
  def combine(m: IPS) = for {
    (a, m2) <- m
    (b, m3) <- m2
    (c, s4) <- m3
    d <- s4
  } yield (a, b, c, d)

  def process(ss: Seq[String]): IPS =
    ss.flatMap(stringToIP2)
      .foldLeft(empty) { (map, ip) =>
        map.updatedWith(ip.a) {
          case Some(a) => ???
          case None    => Some(map3(ip.b, ip.c, ip.d))
        }
      }
}

class IPv4Test extends AnyFunSpec with Matchers {
  it("1") {
    import IPv4._
    
    val src = Seq(
      "1.2.3.4",
      "1.2.3.5"
    )
    val dst = Map(1 -> Map(2 -> Map(3 -> Set(4,5))))
    val processed: IPS = process(src)
    processed shouldEqual dst
  }
}
