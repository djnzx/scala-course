package essential

object X116 extends App {
  sealed trait Tree
  final case class Leaf(value: Int) extends Tree
  final case class Node(left: Tree, right: Tree) extends Tree

  object TreeOps {
    def sum(t: Tree): Int = t match {
      case Leaf(v) => v
      case Node(l, r) => sum(l) + sum(r)
    }
    def double(t: Tree): Tree = t match {
      case Leaf(v) => Leaf(v * 2)
      case Node(l, r) => Node(double(l), double(r))
    }
  }
  val node: Tree = Node(Leaf(10), Leaf(20))
  println(node)
  println(TreeOps.sum(node))
  println(TreeOps.double(node))
}

object X116v2 extends App {
  trait Tree {
    def sum: Int
    def double: Tree
  }
  final case class Leaf(value: Int) extends Tree {
    override def sum: Int = value
    override def double: Tree = Leaf(value * 2)
  }
  final case class Node(left: Leaf, right: Leaf) extends Tree {
    override def sum: Int = left.sum + right.sum
    override def double: Tree = Node(left.double.asInstanceOf, right.double.asInstanceOf)
  }
}
