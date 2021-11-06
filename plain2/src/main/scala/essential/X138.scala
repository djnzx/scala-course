package essential

object X138 extends App {

  sealed trait Tree[A] {
    // to convert tree from type A to type B
    // we must provide two functions
    // A => B - to convert leaf[A] to leaf[B]
    // and (B,B) => B to convert node[A] to node[B]
    def fold[B](node: (B, B) => B, leaf: A => B): B
  }
  final case class Leaf[A](value: A) extends Tree[A] {
    override def fold[B](node: (B, B) => B, leaf: A => B): B = leaf(value)
  }
  final case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A] {
    override def fold[B](node: (B, B) => B, leaf: A => B): B = node(left.fold(node, leaf), right.fold(node, leaf))
  }

  val tree1: Tree[String] =
    Node(
      Node(
        Leaf("To"), Leaf("iterate")),
      Node(
        Node(
          Leaf("is"), Leaf("human,")
        ),
        Node(
          Leaf("to"),
          Node(
            Leaf("recurse"), Leaf("divine")
          )
        )
      )
    )
  val folded1: String = tree1.fold[String]((a, b) => s"$a + $b", s => s"<$s>")
  println(folded1)

  val tree2 = Node(
    Node(Leaf(1), Leaf(2)),
    Node(Leaf(3), Leaf(4))
  )
  println(tree2)
  val folded2 = tree2.fold[String]((a,b)=>s"$a ~ $b", i => (i*2).toString)
  println(folded2)
}
