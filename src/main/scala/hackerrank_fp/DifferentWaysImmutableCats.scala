package hackerrank_fp

import cats.Foldable
import cats.data.State
import cats.data.State._
import cats.instances.list._

/**
  * https://www.hackerrank.com/challenges/different-ways-fp/problem
  * with Immutable Map Cats Library
  * recursion troubles (stack)
  */
object DifferentWaysImmutableCats {

  case class NK(n: Int, k: Int)
  type BD = java.math.BigDecimal
  val t8p7 = new BD(100000007)
  val bd1 = new BD(1)
  type Cache = Map[NK, BD]
  val cache0: Cache = Map[NK, BD]()

  def doCount2(nk: NK): State[Cache, BD] = State[Cache, BD] { cache =>
    val newCache = if (cache.contains(nk)) cache
    else if (nk.k==0 || nk.k==nk.n) cache + (nk -> bd1)
    else (for {
        a <- doCount2(NK(nk.n-1, nk.k-1))
        b <- doCount2(NK(nk.n-1, nk.k))
        c = a.add(b)
        _ <- modify[Cache](_ + (nk -> c))
      } yield c).run(cache).value._1
    (newCache, newCache(nk))
  }

  def doCount(nk: NK): State[Cache, BD] = State[Cache, BD] { cache =>
    if (cache.contains(nk)) {
      (cache, cache(nk))
    } else if (nk.k==0 || nk.k==nk.n) {
      val nCache: Cache = cache + (nk -> bd1)
      (nCache, nCache(nk))
    } else {
      val cs = for {
        a <- doCount(NK(nk.n - 1, nk.k - 1))
        b <- doCount(NK(nk.n - 1, nk.k))
        c = a.add(b)
        _ <- modify[Cache](_ + (nk->c))
      } yield c
      cs.run(cache).value
    }
  }

  case class Step(list: List[Int], cache: Cache)
  // Scala Cats Foldable implementation
  def process(cases: List[NK]): List[Int] =
  Foldable[List].foldLeft(cases, Step(List.empty, cache0)) { (acc, a) =>
    val (newCache, rbd) = doCount2(a).run(acc.cache).value
    Step(rbd.remainder(t8p7).intValueExact :: acc.list, newCache)
  }.list

  // plain Scala implementation
  def process1(cases: List[NK]): List[Int] =
    cases.foldLeft( Step(List.empty, cache0) ) { (acc, a) =>
      val (newCache, rbd) = doCount(a).run(acc.cache).value
      Step(rbd.remainder(t8p7).intValueExact :: acc.list, newCache)
    }.list//.reverse

  def body(readLine: => String): Unit = {
    val N: Int = readLine.toInt

    def readCase: NK = readLine.split(" ").map(_.toInt) match { case Array(a, b) => NK(a, b) }
    @scala.annotation.tailrec
    def addLines(n: Int, acc: List[NK]): List[NK] = n match {
      case 0 => acc//.reverse
      case _ => addLines(n-1, readCase::acc)
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
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File(fname))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
