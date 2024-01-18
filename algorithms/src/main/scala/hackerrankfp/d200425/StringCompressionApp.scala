package hackerrankfp.d200425

/** https://www.hackerrank.com/challenges/string-compression/problem */
object StringCompressionApp {

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  def process(s: String): String = {

    @scala.annotation.tailrec
    def go(tail: List[Char], prev: Char, cnt: Int, acc: List[(Char, Int)]): List[(Char, Int)] = tail match {
      case Nil => (prev, cnt) :: acc
      case h :: t =>
        if (h == prev) go(t, h, cnt + 1, acc)
        else go(t, h, 1, (prev, cnt) :: acc)
    }

    val l: List[Char] = s.toList

    go(l.tail, l.head, 1, Nil)
      .reverse
      .foldLeft("") { (a, item) =>
        if (item._2 == 1) s"$a${item._1}" else s"$a${item._1}${item._2}"
      }
  }

  def body(readLine: => String): Unit = {
    val s = readLine

    val r = process(s)
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200425_04/compress.txt"
  def main_file(p: Array[String]): Unit = {
    scala
      .util
      .Using(
        scala.io.Source.fromFile(new java.io.File(fname)),
      ) { src =>
        val it = src.getLines().map(_.trim)
        body { it.next() }
      }
  }

}
