package hackerrankfp.d200423_03

/**
  * https://www.hackerrank.com/challenges/range-minimum-query/problem
  */
object RangeMinimumQueryApp {

  implicit class StringToOps(s: String) {
    def splitToInt: Array[Int] = s.split(" ").map(_.toInt)
    def toVectorInt: Vector[Int] = splitToInt.toVector
    def toListInt: List[Int] = splitToInt.toList
    def toTuple2Int: (Int, Int) = { val a = splitToInt; (a(0), a(1)) }
  }

  case class Node(left: Int, right: Int, min: Int)

  class SegmentTree(values: Vector[Int]) {
    private val nodes = Array.fill[Node](4*values.size)(null)
    build(1, 0, values.length-1)

    private def build(index: Int, left: Int, right: Int): Node = {
      val min = if (left == right) {
        values(left)
      } else {
        val median = (left+right)/2;
        val l = build(2*index, left, median);
        val r = build(2*index+1, median+1, right);
        Math.min(l.min, r.min)
      }
      val node = Node(left, right, min)
      nodes(index) = node
      node
    }

    def findMin(left: Int, right: Int): Int = findMin(1, left, right)

    private def findMin(index: Int, left: Int, right: Int): Int = {
      val current = nodes(index)
      if      (contains(current, left, right)) current.min
      else if (intersects(current, left, right)) scala.math.min(
        findMin(2 * index, left, right),
        findMin(2 * index + 1, left, right)
      )
      else Integer.MAX_VALUE
    }

    private def intersects(current: Node, left: Int, right: Int): Boolean =
      left <= current.left && current.left <= right ||
        current.left <= left && left <= current.right

    private def contains(node: Node, left: Int, right: Int): Boolean =
      left <= node.left && node.right <= right
  }

// naive approach, complexity N^2
//  def process_naive(st: SegmentTree, mnx: (Int, Int)): Int =
//    (mnx._1 to mnx._2).foldLeft(Integer.MAX_VALUE) { (min, idx) => scala.math.min(min, data(idx)) }

  /**
    * Segment tree, complexity Log N
    */
  def process(st: SegmentTree, mnx: (Int, Int)): Int =
    st.findMin(mnx._1, mnx._2)

  def body(readLine: => String) = {
    val N: Int = readLine.toVectorInt(1)
    val data: Vector[Int] = readLine.toVectorInt
    val st = new SegmentTree(data)

    (1 to N)
      .map { _ => process(st, readLine.toTuple2Int) }
      .foreach { println }
  }

  /**
    * main implementation for reading from the console
    */
  def main(p: Array[String]): Unit = {
    body { scala.io.StdIn.readLine }
//    main_f(p)
  }

  /**
    * main implementation for reading from the file
    */
  def main_f(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File("src/main/scala/hackerrankfp/d200423_03/segments.txt"))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
