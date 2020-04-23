package hackerrankfp.d200423_03

object IsBST {

  case class Node(data: Int, l: Node, r:Node)

  var root: Node = _

  def isBST(root: Node, prev: Int): Boolean =
    if (root == null) true else {
      if      (!isBST(root.l, prev)) false
      else if (root.data <= prev) false
      else isBST(root.r, root.data)
    }

  def isBST(root: Node): Boolean =
    isBST(root, Integer.MIN_VALUE)

  def add(item: Int): Node = ???

  def process(line1: String, line2: String): Boolean = {
    line2.split(" ") map { _.toInt } foreach add
    isBST(root)
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
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File("src/main/scala/hackerrankfp/d200423_03/tree.txt"))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
