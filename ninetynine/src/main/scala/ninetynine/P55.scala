package ninetynine

object P55 {

  sealed abstract class Tree[+A]
  case class Node[+A](value: A, l: Tree[A], r: Tree[A]) extends Tree[A] {
    override def toString = s"T($value $l $r)"
  }
  case object End extends Tree[Nothing] {
    override def toString: String = "."
  }
  object Node {
    def apply[A](value: A): Node[A] = new Node(value, End, End)
  }
  
}

class P55Spec extends NNSpec {
  import P55._
  import Predef.{println => pl}
  
  it("") {
    val t = Node(5, End, End)
    println(t)
    pl(t)
  }
}
