package hackerrankfp.d200423

import scala.language.postfixOps

/**
  * https://www.hackerrank.com/challenges/valid-bst/problem
  * https://www.geeksforgeeks.org/check-if-a-given-array-can-represent-preorder-traversal-of-binary-search-tree/
  */
object IsPreorderBST {

  case class XState(stack: List[Int], out: Boolean, root: Int)
  def isBST(data: Vector[Int], length: Int): Boolean =
    data.foldLeft( XState(Nil, out = true, Integer.MIN_VALUE) ) { (x, item) =>
      if (!x.out) x else
        if (item < x.root) x.copy(out = false) else {

          def refine(s0: List[Int], r0: Int): (List[Int], Int) =
            if (s0.nonEmpty && s0.head < item) refine(s0.tail, s0.head)
            else (s0, r0)

          val (s2, r2) = refine(x.stack, x.root)
          x.copy(stack = item :: s2, root = r2)
        }
    }
      .out


  def process(line1: String, line2: String): Boolean = {
    val data = line2.split(" ") map { _.toInt } toVector

    isBST(data, data.length)
  }

  def body(readLine: => String) = {
    (1 to readLine.trim.toInt)
      .map { _ => process(readLine, readLine) }
      .map { if (_) "YES" else "NO" }
      .foreach { println }
  }

  /**
    * main implementation for reading from the console
    */
  def main(p: Array[String]): Unit = {
//    body { scala.io.StdIn.readLine }
    main_f(p)
  }

  /**
    * main implementation for reading from the file
    */
  def main_f(p: Array[String]): Unit = {
    scala.util.Using(
      scala.io.Source.fromFile(new java.io.File("src/main/scala/hackerrankfp/d200423_03/tree.txt"))
    ) { src =>
      val it = src.getLines().map(_.trim)
      body { it.next() }
    }
  }

}
