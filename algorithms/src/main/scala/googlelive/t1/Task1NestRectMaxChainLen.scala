package googlelive.t1

import graphs.impls.LongestPathImpl
import graphs.rep.DiGraphA

import scala.collection.mutable

/**
  * since the task was to find the maximum length
  * we can stop generation the all sequences
  * we need to calculate just the deepest one
  */
object Task1NestRectMaxChainLen extends App {
  import Task1Domain._
  import Timed.timed

  /**
    * we collect all rectangles which are bigger
    * to map: `Map[R, Set[R]]` 
    * 
    * a>b, a>c, a>d, b>e
    * 
    * becomes:
    * 
    * a->(b,c,d) b->(e)
    * 
    * O(N^2^)
    */
  def compareToMap(rs: IndexedSeq[R]) = {
    val dp = mutable.Map.empty[R, Set[R]]

    rs.indices.foreach { i =>
      rs.indices
        .foreach { j =>
          if (gt(rs(i), rs(j))) {
//            println(s"${rs(i)} > ${rs(j)}")
            dp.updateWith(rs(i)) {
              case None     => Some(Set(rs(j)))
              case Some(ss) => Some(ss + rs(j))
            }
          }
        }
    }

    dp
  }
  
  def chainsFromMap(m: mutable.Map[R, Set[R]]) = {
    def chainsFor(r: R): List[List[R]] = m.contains(r) match {
      case false => List(Nil)
      case true => m(r).flatMap { ch => chainsFor(ch).map(r :: _) }.toList
    }

    m.keys.flatMap(p => chainsFor(p).map(p :: _))
  }

  def maxLenFromMap(m: mutable.Map[R, Set[R]]) = {
    def maxLenFor(r: R): Int = m.get(r) match {
      case None => 0
      case Some(x) => x.map { x => 1 + maxLenFor(x) }.max
    }

    m.keys.map(x => 1 + maxLenFor(x)).max
  }

  /**
    * since we don't need the particular chain,
    * we only need it length,
    * we can fall to ID of a rectangle
    * 
    * O(N^2^)
    */
  def compareToArray(rs: IndexedSeq[R]) = {
    val dp = Array.ofDim[List[Int]](rs.length)
    dp.indices.foreach(dp(_) = Nil)

    rs.indices.foreach { i =>
      rs.indices
        .filter(j => gt(rs(i), rs(j)))
        .foreach { j =>
//          println(s"${rs(i)} > ${rs(j)}")
          dp(i) = j :: dp(i)
        }
    }

    dp
  }

  def maxLenFromArray(m: Array[List[Int]]) = {
    def maxLenFor(r: Int): Int = m(r) match {
      case Nil => 1
      case ls  => 1 + ls.map(maxLenFor).max
    }
    m.indices.map(maxLenFor).max
  }

  val rects = rndRects(110).toIndexedSeq
  val ordering: Array[List[Int]] = compareToArray(rects)

  println(s"Vertices count:${ordering.count(_.nonEmpty)}")
  println(s"Total relations number: ${ordering.map(_.length).sum}")

  // 4.5s (len = 19)
  val (maxLen, spent) = timed(maxLenFromArray(ordering))
  println(s"Max length: $maxLen")
  println(s"time:${spent}ms")
  
  val g = DiGraphA.from(ordering)
  val lp = new LongestPathImpl(g)

  println("DFS:")
  // 47s - 10x slower
  val (path, spent2) = timed(lp.longestPath)
  println(s"Path: $path")
  println(s"Length: ${path.length}")
  println(s"time:${spent2}ms")

  // 42s - 7x slower
  println("DFS length only:")
  val (maxLen3, spent3) = timed(lp.longestLength)
  println(s"Length: $maxLen3")
  println(s"time:${spent3}ms")

}
