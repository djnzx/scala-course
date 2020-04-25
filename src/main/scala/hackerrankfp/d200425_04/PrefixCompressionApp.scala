package hackerrankfp.d200425_04

/**
  * https://www.hackerrank.com/challenges/prefix-compression/problem
  */
object PrefixCompressionApp {

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  case class Prefix(keep: Boolean, pre: List[Char], s1: List[Char], s2: List[Char])
  val zero: Prefix = Prefix(keep = true, Nil, Nil, Nil)

  def process(s1: String, s2: String): Prefix =
    (0 until math.max(s1.length, s2.length))
      .foldLeft(zero: Prefix) { (a, idx) =>
        if (!a.keep) a else
          if (idx<s1.length && idx<s2.length && s1(idx)==s2(idx))
            a.copy(pre = s1(idx) :: a.pre)
          else Prefix(keep = false, a.pre, s1.substring(idx).toList.reverse, s2.substring(idx).toList.reverse)
      }

  def body(readLine: => String): Unit = {
    val r = process(readLine, readLine)
    print(List(
      s"${r.pre.length} ${r.pre.reverse.mkString}",
      s"${r.s1.length} ${r.s1.reverse.mkString}",
      s"${r.s2.length} ${r.s2.reverse.mkString}",
    ).mkString("\n"))
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200425_04/prefix.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
