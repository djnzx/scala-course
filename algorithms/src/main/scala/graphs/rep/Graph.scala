package graphs.rep

/**
  * Undirected Graph
  * a - b means a -> b && b -> a
  */
trait Graph {

}

/**
  * Directed Graph
  * a -> b DOESN'T MEAN b -> a
  */
trait DiGraph {
  def add(v: Int, w: Int): Unit
//  def remove(v: Int, w: Int): Unit
  def v: Int
  def adjTo(v: Int): Seq[Int]
  def vertices: Range
}

/**
  * Undirected Edge Weighted Graph
  * a - b costs X, means a -> b cost X && b -> a cost X
  */
trait EWGraph {
  
}

/**
  * Directed Edge Weighted Graph
  * a -> b costs X, DOESN'T MEAN b -> a costs X
  */
trait DiEWGraph {
  
}
