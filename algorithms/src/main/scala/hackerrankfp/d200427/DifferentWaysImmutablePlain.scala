package hackerrankfp.d200427

/** https://www.hackerrank.com/challenges/different-ways-fp/problem with Immutable Map Plain Scala
  */
object DifferentWaysImmutablePlain {
  case class NK(n: Int, k: Int)
  type BD = java.math.BigDecimal
  val t8p7 = new BD(100000007)
  val bd1 = new BD(1)
  type Cache = Map[NK, BD]
  val cache0: Cache = Map[NK, BD]()

  def count(nk: NK, cache: Cache): (BD, Cache) = {
    val newCache =
      if (cache.contains(nk)) cache
      else if (nk.k == 0 || nk.k == nk.n) cache + (nk -> bd1)
      else {
        val (a, nc2) = count(NK(nk.n - 1, nk.k - 1), cache)
        val (b, nc3) = count(NK(nk.n - 1, nk.k), nc2)
        val c = a.add(b)
        nc3 + (nk -> c)
      }
    (newCache(nk), newCache)
  }

  case class Step(list: List[Int], cache: Cache)
  def process(cases: List[NK]): List[Int] =
    cases
      .foldLeft(Step(List.empty, cache0)) { (acc, a) =>
        count(a, acc.cache) match {
          case (rbd, newCache) =>
            Step(rbd.remainder(t8p7).intValueExact :: acc.list, newCache)
        }
      }
      .list // .reverse

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    def readCase: NK = readLine.split(" ").map(_.toInt) match {
      case Array(a, b) => NK(a, b)
      case _           => ???
    }
    @scala.annotation.tailrec
    def addLines(n: Int, acc: List[NK]): List[NK] = n match {
      case 0 => acc // .reverse
      case _ => addLines(n - 1, readCase :: acc)
    }
    val points = addLines(N, Nil)
    val r = process(points) mkString "\n"
    println(r)
  }

  def main(p: Array[String]): Unit = {
    //  body { scala.io.StdIn.readLine }
    main_file(p)
  }

  val fname = "src/main/scala/hackerrankfp/d200427_06/differentways.txt"
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
