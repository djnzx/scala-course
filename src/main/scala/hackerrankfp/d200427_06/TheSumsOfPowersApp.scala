package hackerrankfp.d200427_06

/**
  * https://www.hackerrank.com/challenges/functional-programming-the-sums-of-powers/problem
  */
object TheSumsOfPowersApp {

  def process(n: Int, x: Int) = {
    def pow(n: Int, p: Int): Int = (1 to p).foldLeft(1) { (a, _) => a * n }
    case class NP(n: Int, np: Int)
    val max = math.ceil(math.sqrt(n)).toInt
    val powers: List[NP] = (1 to max).map { n => NP(n, pow(n,x)) }.toList

    def split(options: List[NP], sub: Int, acc: List[Int]): List[Option[List[Int]]] =
      if (sub == n) List(Some(acc))
      else if (sub > n) List(None)
      else options match {
        case Nil  => List(None)
        case h::t if sub + h.np > n => split(t, sub, acc)
        case h::t => split(t, sub + h.np, h.n::acc):::split(t, sub, acc)
      }

    split(powers, 0, Nil).flatten
  }

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt
    val X: Int = readLine.toInt
    val r = process(N, X)//.size
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
