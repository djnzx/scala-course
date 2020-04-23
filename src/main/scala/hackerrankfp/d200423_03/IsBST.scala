package hackerrankfp.d200423_03

/**
  * https://www.hackerrank.com/challenges/valid-bst/problem
  * https://www.geeksforgeeks.org/check-if-a-given-array-can-represent-preorder-traversal-of-binary-search-tree/
  */
object IsBST {


  def process(line1: String, line2: String): Boolean = {
//    line2.split(" ") map { _.toInt } foreach add
//    isBST(root)
    true
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
