package x060essential

object X138 extends App {
  sealed trait Tree[A] {
    def fold[B](node: (B, B) => B, leaf: A => B): B
  }
  final case class Leaf[A](value: A) extends Tree[A] {
    override def fold[B](node: (B, B) => B, leaf: A => B): B = leaf(value)
  }
  final case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A] {
    override def fold[B](node: (B, B) => B, leaf: A => B): B = node(left.fold(node, leaf), right.fold(node, leaf))
  }
  val tree: Tree[String] =
    Node(Node(Leaf("To"), Leaf("iterate")),
      Node(Node(Leaf("is"), Leaf("human,")),
        Node(Leaf("to"), Node(Leaf("recurse"), Leaf("divine")))
      )
    )
  val folded: String = tree.fold[String]((a, b) => s"$a $b", s => s"<$s>")
  print(folded)
}
