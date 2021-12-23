package tree

import Domain._
import Implementations._

object Tests extends App {

  val t1 = Branch(
    Branch(Leaf(1), Leaf(2)),
    Branch(Leaf(3), Branch(Leaf(4), Leaf(5))),
  )

  val t2a = mapTree(t1)(_ * 10)
  val t2b = mapTreeTR(t1)(_ * 10)

  println("original")
  pprint.pprintln(t1)

  println("mapped stack recursive")
  pprint.pprintln(t2a)

  println("mapped tail recursive")
  pprint.pprintln(t2b)

}
