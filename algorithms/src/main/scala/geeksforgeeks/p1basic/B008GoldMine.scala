package geeksforgeeks.p1basic

import tools.spec.ASpec
import tools.ArrayTools._
import tools.fmt.Fmt.printMatrix

/**
  * https://www.geeksforgeeks.org/gold-mine-problem/
  */
object B008Common {
  /** all possible next moves from particular point */
  def nextMoves(a: Array[Array[Int]], pt: (Int, Int)) = pt match { case (x, y) =>
    Seq(
      a.orNone(y-1)(x+1),
      a.orNone(y  )(x+1),
      a.orNone(y+1)(x+1),
    ).flatten
  }
}
/** pure FP version, complexity exponential */
object B008GoldMineFP {
  import B008Common._
  
  def maxChain(a: Array[Array[Int]]) = {
    /** all chain from particular point */
    def chainsFrom(pt: (Int, Int)): Seq[List[(Int, Int)]] = nextMoves(a, pt) match {
      case Nil  => List(List(pt))
      case next => next.flatMap { chainsFrom(_).map(pt::_) }
    }
    /** all chains on the board */
    def allChains = a.indices.flatMap { chainsFrom(0, _) }
    /** all chains with values */
    def allChainsV = allChains.map { _.map { case (x,y) => a(y)(x)} } 
    allChainsV.map(_.sum).max
  }
}
/** DP version */
object B008GoldMine {
  import B008Common._
  
  def maxChain(a: Array[Array[Int]]) = {
    val H = a.length
    val W = a(0).length
    val maxes = Array.ofDim[Int](H, W)
    
    def maxChild(pt: (Int, Int)) =
      nextMoves(a, pt).map { case (x, y) => a(y)(x) }.max
      
    a.indices.foreach { y =>
      maxes(y)(2) = maxChild(1, y)
    }
    printMatrix(a, "a")
    printMatrix(maxes, "max")
    
    /** all chain from particular point */
    def chainsFrom(pt: (Int, Int)): Seq[List[(Int, Int)]] = nextMoves(a, pt) match {
      case Nil  => List(List(pt))
      case next => next.flatMap { chainsFrom(_).map(pt::_) }
    }
    /** all chains on the board */
    def allChains = a.indices.flatMap { chainsFrom(0, _) }
    /** all chains with values */
    def allChainsV = allChains.map { _.map { case (x,y) => a(y)(x)} } 
    allChainsV.map(_.sum).max
  }
}

class B008GoldMineSpec extends ASpec {
  import B008GoldMine._
  import tools.Data.{a => aa}

  val data = Seq(
    aa(
      aa(1, 2, 3),
      aa(4, 5, 6),
      aa(7, 8, 9)
    ) -> {7+8+9},
    aa(
      aa(1, 3, 3),
      aa(2, 1, 4),
      aa(0, 6, 4)
    ) -> 12,
    aa(
      aa(1, 3, 1, 5),
      aa(2, 2, 4, 1),
      aa(5, 0, 2, 3),
      aa(0, 6, 1, 2)
    ) -> 16,
    aa(
      aa(10, 33, 13, 15),
      aa(22, 21, 4, 1),
      aa(5, 0, 2, 3),
      aa(0, 6, 14, 2)
    ) -> 83,
  )
  
  it("1") {
    runAllD(data, maxChain)
  }
  
}
