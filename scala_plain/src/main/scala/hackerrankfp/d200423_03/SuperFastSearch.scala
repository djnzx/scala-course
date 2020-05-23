package hackerrankfp.d200423_03

import scala.util.Using

/**
  * Knuth–Morris–Pratt algorithm
  * https://en.wikipedia.org/wiki/Knuth–Morris–Pratt_algorithm
  * https://www.hackerrank.com/challenges/kmp-fp/problem
  */
object SuperFastSearch {

  def buildMeta(word: String): Vector[Int] = {
    val meta = Array.fill[Int](word.length + 1)(-1)
    var pos = 1
    var cnd = 0

    while (pos < word.length) {
      if (word(pos) == word(cnd)) {
        meta(pos) = meta(cnd)
      } else {
        meta(pos) = cnd
        cnd = meta(cnd)
        while (cnd >= 0 && word(pos) != word(cnd)) cnd = meta(cnd)
      }
      pos +=1
      cnd +=1
    }
    meta(pos) = cnd
    meta.toVector
  }

  def process(src: String, w: String): Boolean = {
    val meta = buildMeta(w)
    var j = 0  // source (S) index
    var k = 0  // pattern (W) index
    val indexes = scala.collection.mutable.ListBuffer.empty[Int]

    while (j < src.length) {
      if (w(k) == src(j)) {
        j += 1
        k += 1
        if (k == w.length) {
          indexes += j - k
          k = meta(k)
        }
      } else {
        k = meta(k)
        if (k<0) {
          j +=1
          k +=1
        }
      }
    }

    indexes.nonEmpty
  }

  def body(readLine: => String) = {
    (1 to readLine.trim.toInt)
      .map { _ => process(readLine, readLine) }
      .map { if (_) "YES" else "NO" }
      .foreach { println }
  }

//  def main(p: Array[String]): Unit = {
//    body { scala.io.StdIn.readLine }
//  }

  def main(p: Array[String]): Unit = {
    Using(
      scala.io.Source.fromFile(new java.io.File("src/main/scala/hackerrankfp/d200423_03/strings.txt"))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }
}
