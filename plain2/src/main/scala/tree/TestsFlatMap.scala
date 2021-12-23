package tree

import tree.Domain._
import tree.Implementations._

object TestsFlatMap extends App {

  val t1 = Branch(
    Leaf(10),
    Leaf(20),
  )

  val t2 = flatMapTreeTR(t1)(a => Branch(Leaf(a - 1), Leaf(a + 1)))

  println("original")
  pprint.pprintln(t1)

  println("flatMapped tail recursive")
  pprint.pprintln(t2)

}
