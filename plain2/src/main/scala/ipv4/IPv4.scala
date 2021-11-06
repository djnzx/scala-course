package ipv4

import scala.util.Try

object IPv4 {
  /** IP address representation */
  case class IP(a: Int, b: Int, c: Int, d: Int)
  /** syntax to manipulate with IP parts */
  implicit class IPSyntax(private val ip: IP) extends AnyVal {
    import ip._
    def a8  = Set(d)        // last 1
    def a16 = Map(c -> a8)  // last 2
    def a24 = Map(b -> a16) // last 3
  }
  /** parser */
  object IP {
    private def isLen4(a: Array[_]) = a.length == 4
    def parse(s: String) = Option(s)
      .map(_.split("\\."))
      .filter(isLen4)
      .map(_.flatMap(x => x.toIntOption))
      .filter(isLen4)
      .map(_.filter(x => x >=0 && x <= 255))
      .filter(isLen4)
      .map { case Array(a, b, c, d) => IP(a, b, c, d) }
  }

  type IPS = Map[Int, Map[Int, Map[Int, Set[Int]]]]
  class UniqueIPs(val map: IPS = Map.empty) {
    import UniqueIPs._

    /** process ONE IP */
    def process(ip: String): UniqueIPs =
      new UniqueIPs(processToMap(map, ip))

    /** process MANY IPs */
    def process(ips: Seq[String]): UniqueIPs =
      new UniqueIPs(ips.foldLeft(map)(processToMap))

    /** contains by IP */
    def contains(ip: IP): Boolean =
      map.get(ip.a)
        .flatMap(_.get(ip.b))
        .flatMap(_.get(ip.c))
        .exists(_.contains(ip.d))

    /** contains by String */
    def contains(ip: String): Boolean =
      IP.parse(ip).exists(contains)

    /** produce Iterable[IP] from map */
    def flatten = for {
      (a, m2) <- map
      (b, m3) <- m2
      (c, s4) <- m3
      d <- s4
    } yield IP(a, b, c, d)
  }

  object UniqueIPs {

    /** core function to combine map with IP given */
    private def combine(map: IPS, ip: IP) =
      map.updatedWith(ip.a) {
        case None     => Some(ip.a24)
        case Some(ma) => Some(ma.updatedWith(ip.b) {
          case None     => Some(ip.a16)
          case Some(mb) => Some(mb.updatedWith(ip.c) {
            case None     => Some(ip.a8)
            case Some(mc) => Some(mc ++ ip.a8)
          })
        })
      }

    private def processToMap(map: IPS, ip: String) =
      IP.parse(ip)
        .map(combine(map, _))
        .getOrElse(map)

    /** entry point to process group of IP addresses */
    def process(ss: Seq[String]) =
      new UniqueIPs()
        .process(ss)

  }
}
