package hackerrankfp.d200427_06

import scala.collection.mutable

/**
  * https://www.hackerrank.com/challenges/different-ways-fp/problem
  */
object DifferentWays {
  type BD = java.math.BigDecimal
  val K7 = new BD(100000007)
  val bd1 = new BD(1)
  val cache: mutable.Map[(Int, Int), BD] = mutable.Map[(Int, Int), BD]()

  def count(n: Int, k: Int): BD =
    if (k==0) bd1
    else if (k==n) bd1
    else {
      if (!cache.contains((n,k))) {
        val a = count(n-1, k-1)
        val b = count(n-1, k)
        val c = a.add(b)
        cache.put((n,k), c)
      }
      cache((n,k))
    }

  def process(cases: List[(Int, Int)]): List[Int] = cases map { c => count(c._1, c._2).remainder(K7).intValueExact }

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    def readCase: (Int, Int) = readLine.split(" ").map(_.toInt) match { case Array(a, b) => (a, b) }
    @scala.annotation.tailrec
    def addLines(n: Int, acc: List[(Int, Int)]): List[(Int, Int)] = n match {
      case 0 => acc.reverse
      case _ => addLines(n-1, readCase::acc)
    }
    val points = addLines(N, Nil)
    val r = process(points) mkString ("\n")
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200427_06/differentways.txt"
  def main_file(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
