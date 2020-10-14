package google.live.t1

import graphs.impls.LongestPathImpl
import graphs.rep.DiGraphA
import tools.Timed.{printTimed, timed}

/**
  * since the task was to find the maximum length
  * we can stop generation the all sequences
  * we need to calculate just the deepest one
  */
object Task1LongestChainDP extends App {
  import T1Compare._
  import T1Domain._
  import T1Data._
  val FMT = "%.1f"

//  val rectangles = rect9max4
  val rectangles = rndRects(4000).toIndexedSeq
  val ordering = compareAllToArray(rectangles)
  val nVert = ordering.count(_.nonEmpty).toDouble/1000
  val nRel = ordering.map(_.length).sum.toDouble/1000000
  
  println(s"Vertices count: ${FMT}k" format nVert)
  println(nRel formatted s"Total relations number: ${FMT}m")

  /**
    * 700x faster
    * graph-based with DP
    * 10k rectangles produces 25m relations
    * runtime: ~6s
    */
  println("DFS length only:")
  val graph = DiGraphA.from(ordering)
  val lp = new LongestPathImpl(graph)
  
  printTimed(lp.longestLength, "Max length")
  val (path, spent) = timed(lp.longestPath)
  println(s"Longest Path: ${prettyPath(path)}")
  println(s"Spent: ${spent}ms")
}
