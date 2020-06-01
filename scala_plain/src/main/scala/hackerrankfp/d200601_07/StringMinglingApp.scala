package hackerrankfp.d200601_07

/**
  * https://www.hackerrank.com/challenges/string-mingling/problem
  */
object StringMinglingApp {

  def mingle(p: String, q: String): String =
    (p zip q)
      .map { case (a, b) => s"$a$b" }
      .mkString

  def main(args: Array[String]): Unit = {
    val p = scala.io.StdIn.readLine()
    val q = scala.io.StdIn.readLine()
    val r = mingle(p, q)
    println(r)
  }
  
}
