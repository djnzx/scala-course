package geeksforgeeks.p1basic

import tools.spec.ASpec
import tools.ArrayTools._

/**
  * https://www.geeksforgeeks.org/gold-mine-problem/
  */
object B008Common {
  /** all possible next moves from particular point in the given array*/
  def nextMoves(a: Array[Array[Int]], pt: (Int, Int)) = pt match { case (x, y) =>
    Seq(
      a.ptOrNone(y-1)(x+1),
      a.ptOrNone(y  )(x+1),
      a.ptOrNone(y+1)(x+1),
    ).flatten
  }
  /**
    * all possible next moves from particular point in the given array,
    * y-dim version only
    *
    * @return - Seq[Int] - indexes
    */
  def nextMoves(a: Array[Int], y: Int) =
    Seq(
      a.idxOrNone(y-1),
      a.idxOrNone(y),
      a.idxOrNone(y+1),
    ).flatten
}
/** 
  * pure FP version,
  * calculates all possible chains
  * and mapping them to values and to length
  * 
  * complexity - exponential
  */
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
/**
  * pure FP version,
  * DP implementation, 
  * but keeps track only about the max sum
  * 
  * complexity - linear
  */
object B008GoldMineDPFP {
  import B008Common._

  /**
    * having column (Y)
    * calculate possible moves y => (y-1, y, y+1)
    * and extracting the max from them
    */
  def maxFrom(y: Int, a: Array[Int]) = nextMoves(a, y) match {
    case Nil  => 0 // no more moves
    case idxs => idxs.map(a(_)).max
  }

  def colToMax(a: Array[Int]) =
    a.indices.map(i => maxFrom(i, a)).toArray

  def maxChain(aa: Array[Array[Int]]) = {
    val H = aa.length
    val vIndices = aa.indices
    val W = aa(0).length
    val hIndexesInv = (0 until W).map { x => W - 1 - x }
    val last = Array.ofDim[Int](H)

    hIndexesInv
      .foldLeft(last) { (prev, xi) => // 2,1 on 3 dim (hor)
        val maxes = colToMax(prev)    // max avail from cur pos
        vIndices.map { yi =>          // 0,1,2 on 3 dim (ver)
          aa(yi)(xi) + maxes(yi)
        }.toArray
      }
      .max
  }
}

object B008GoldMineDPFPTraced {
  import B008Common._

  /**
    * having column (Y)
    * calculate possible moves y => (y-1, y, y+1)
    * and extracting the max from them (with path)
    */
  def maxFrom(y: Int, a: Array[(Int, List[Int])]) =
    nextMoves(a.map(_._1), y) match {
      case Nil  => (0, Nil)
      case idxs => idxs.map(a(_)).maxBy(_._1)
    }

  def colToMax(a: Array[(Int, List[Int])]) =
    a.indices.map(i => maxFrom(i, a)).toArray

  def maxChainTraced(aa: Array[Array[Int]]): (Int, List[Int]) = {
    val H = aa.length
    val vIndices = aa.indices
    val W = aa(0).length
    val hIndexesInv = (0 until W).map { x => W - 1 - x }
    val initial = Array.ofDim[Int](H).map(_ -> List.empty[Int])

    hIndexesInv
      .foldLeft(initial) { (prev, xi) =>
        val maxes = colToMax(prev)
        vIndices.map { yi =>
          val x = aa(yi)(xi)
          val (mx, path) = maxes(yi)
          (x + mx, x :: path)
        }.toArray
        
      }
      .maxBy(_._1)
  }
  
  def maxChain(a: Array[Array[Int]]) = maxChainTraced(a)._1
}

class B008GoldMineSpec extends ASpec {
  import B008Common._
  import tools.Data.{a => aa}

  val data = Seq(
    aa(
      aa(1, 2, 3),
      aa(4, 5, 6),
      aa(7, 8, 9)
    ) -> (24, List(7,8,9)),
    aa(
      aa(1, 3, 3),
      aa(2, 1, 4),
      aa(0, 6, 4)
    ) -> (12, List(2, 6, 4)),
    aa(
      aa(1, 3, 1, 5),
      aa(2, 2, 4, 1),
      aa(5, 0, 2, 3),
      aa(0, 6, 1, 2)
    ) -> (16, List(5,2,4,5)),
    aa(
      aa(10, 33, 13, 15),
      aa(22, 21, 4, 1),
      aa(5, 0, 2, 3),
      aa(0, 6, 14, 2)
    ) -> (83, List(22,33,13,15)),
  )

  it("nextMoves on 1D") {
    val a = Array(10,20,30,40)
    nextMoves(a, 0) shouldEqual aa(0,1)   // y=0 => moves: 0,1
    nextMoves(a, 1) shouldEqual aa(0,1,2) // y=1 => moves: 0,1,2
    nextMoves(a, 2) shouldEqual aa(1,2,3) // y=2 => moves: 1,2,3
    nextMoves(a, 3) shouldEqual aa(2,3)   // y=3 => moves:   2,3
  }

  it("maxFrom") {
    import B008GoldMineDPFP._
    val a = Array(10,20,30,40)
    maxFrom(0, a) shouldEqual 20
    maxFrom(1, a) shouldEqual 30
    maxFrom(2, a) shouldEqual 40
    maxFrom(3, a) shouldEqual 40
  }

  it("colToMax") {
    import B008GoldMineDPFP._
    val a = Array(10,20,30,40)
    colToMax(a) shouldEqual Array(20,30,40,40)
  }

  it("maxChain") {
    val impls = Seq(
      B008GoldMineFP.maxChain _,
      B008GoldMineDPFP.maxChain _,
      B008GoldMineDPFPTraced.maxChain _,
    )
    runAllSD(data.map(x => (x._1, x._2._1)), impls)
  }
  
  it("maxChain with trace") {
    import B008GoldMineDPFPTraced._
    runAll(data, maxChainTraced)
  }

}
