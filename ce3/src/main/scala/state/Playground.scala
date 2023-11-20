package state

import cats.data.State

object Playground extends App {

  sealed trait Tree[A]
  case class Leaf[A](x: A)                   extends Tree[A]
  case class Node[A](l: Tree[A], r: Tree[A]) extends Tree[A]

  val tree = Node(
    Node(Leaf("a"), Leaf("b")),
    Leaf("c")
  )

  //               S    A
  val next: State[Int, Int] = State((s: Int) => (s + 1) -> s)

  //                             S      A
  def mlabel(t: Tree[_]): State[Int, Tree[Int]] = t match {
    case Leaf(_) => next.map(x => Leaf(x))
    case Node(lt, rt) => for {
      l <- mlabel(lt)
      r <- mlabel(rt)
    } yield Node(l, r)
  }

  def label(tree: Tree[_], start: Int) = mlabel(tree).runA(start).value

  val tree2 = label(tree, 10)
  pprint.pprintln(tree)
  pprint.pprintln(tree2)
}
