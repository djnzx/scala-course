package graphs.impls

import graphs.ops.{LongestLength, LongestPath}
import graphs.rep.DiGraph

/**
  * graph must be acyclic !!!
  */
class LongestPathImpl(g: DiGraph) extends LongestPath with LongestLength {

  case class Item(len: Int, path: List[Int]) {
    def add(x: Int) = Item(len + 1, x :: path)
  }

  private def visitNext(v: Int, proc: Item, pathTo: Array[Item], graph: DiGraph): Unit = {
    if (pathTo(v).len >= proc.len) return
    pathTo(v) = proc
    graph.adjTo(v).foreach { vi => visitNext(vi, proc.add(vi), pathTo, graph) }
  }
  /** 
    * DP version
    * but 6x times slower than just `longestLength`
    * because actually collects all longest paths
    */
  def allPaths = {
    val pathTo = Array.fill[Item](g.v)(Item(0, Nil))
    
    /** trace all the paths */
    g.vertices.foreach(v => visitNext(v, Item(1, List(v)), pathTo, g))
    pathTo.map(_.path).map(_.reverse)
  }
  
  override def longestPath: Seq[Int] = {
    val (_, lastV) = longestLength
    val pathTo = Array.fill[Item](g.v)(Item(0, Nil))

    /** trace only one on reversed graph */
    visitNext(lastV, Item(1, List(lastV)), pathTo, g.reverse)
    pathTo.maxBy(_.len).path
  }

  /** DP version */
  override def longestLength: (Int, Int) = {
    val maxTo = Array.ofDim[Int](g.v)

    def visitNext(v: Int, len: Int): Unit = {
      if (maxTo(v) >= len) return 
      maxTo(v) = len
      g.adjTo(v).foreach(visitNext(_, len+1))
    }
    
    g.vertices.foreach(visitNext(_, 1))
    maxTo.zipWithIndex.maxBy(_._1)
  }
  
}
