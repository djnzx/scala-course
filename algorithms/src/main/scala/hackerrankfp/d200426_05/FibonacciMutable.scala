package hackerrankfp.d200426_05

/**
  * https://www.hackerrank.com/challenges/fibonacci-fp/problem
  */
object FibonacciMutable {
  type BD = java.math.BigDecimal
  val bd0 = new BD(0)
  val bd1 = new BD(1)
  val bound = new BD(100_000_000+7)

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  def buildNfibo(total: Int): Array[BD] = {
    val fibos = Array.fill[BD](total)(null)

    def fibo(n: Int): BD = n match {
      case 0 => fibos(n) = new BD(n); fibos(n)
      case 1 => fibos(n) = new BD(n); fibos(n)
      case _ => {
        if (fibos(n-1) == null) fibos(n-1) = fibo(n-1)
        if (fibos(n-2) == null) fibos(n-2) = fibo(n-2)
        fibos(n-1).add(fibos(n-2))
      }
    }

    fibo(total)
    fibos
  }

  def process(source: List[Int]) = {
    val max = source.max
    val fibo = buildNfibo(max+1)
    source map { fibo(_).remainder(bound).intValueExact toString } mkString "\n"
  }

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    def readNumber: Int = readLine.trim.toInt
    def addLines(n: Int, acc: List[Int]): List[Int] = n match {
      case 0 => acc.reverse
      case _ => addLines(n-1, readNumber::acc)
    }

    val rq = addLines(N, Nil)
    val r = process(rq)
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200426_05/fibo.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
