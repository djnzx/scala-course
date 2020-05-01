package hackerrankfp.d200427_06

/**
  * https://www.hackerrank.com/challenges/functional-programming-the-sums-of-powers/problem
  */
object TheSumsOfPowersApp {

  // DP
  def numberOfWays(x: Int, n: Int): Int = {
    val max = math.floor(math.pow(x, 1.0 / n)).toInt
    val powers = (1 to max).map(c => math.pow(c, n).toInt).toList
    def count(s: Int, candidates: List[Int]): Int = candidates match {
      case Nil => 0
      case c :: cs =>
        if (c == s) 1
        else if (c > s) count(s, cs)
        else count(s - c, cs) + count(s, cs)
    }

    count(x, powers)
  }

  def process(n: Int, x: Int) = {
    def pow(n: Int, p: Int): Int = (1 to p).foldLeft(1) { (a, _) => a * n }
    case class NP(n: Int, np: Int)
    val max = math.ceil(math.sqrt(n)).toInt
    val powers: List[NP] = (1 to max).map { n => NP(n, pow(n,x)) }.toList

    def split(options: List[NP], sub: Int, acc: List[Int]): List[Option[List[Int]]] =
      if (sub > n) List(None)
      else (sub-n, options) match {
        case (0, _   ) => List(Some(acc))
        case (_, Nil ) => List(None)
        case (_, h::t) if sub + h.np > n => split(t, sub, acc)
        case (_, h::t) => split(t, sub + h.np, h.n::acc):::split(t, sub, acc)
      }

    split(powers, 0, Nil).flatten
  }

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt
    val X: Int = readLine.toInt
    val r = process(N, X).size
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200427_06/thesumofp.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
