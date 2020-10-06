package hackerrankfp.d200426_05

/**
  * https://www.hackerrank.com/challenges/string-reductions/problem
  * https://www.hackerrank.com/challenges/remove-duplicates/problem
  */
object StringReductions {

  def process(s: String) = {
    @scala.annotation.tailrec
    def go(tail: List[Char], acc: List[Char]): List[Char] = tail match {
      case Nil  => acc.reverse
      case h::t => go(t, if(acc.contains(h)) acc else h::acc)
    }
    go(s.toList, Nil).mkString
  }

  def body(readLine: => String): Unit = {
    val r = process(readLine)
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200426_05/stringred.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
