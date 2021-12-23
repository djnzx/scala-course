package tree

import Domain._
import scala.annotation.tailrec

object Implementations {

  def mapTree[A, B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(a)        => Leaf(f(a))
    case Branch(la, ra) => Branch(mapTree(la)(f), mapTree(ra)(f))
  }

  def flatMapTreeTR[A, B](t: Tree[A])(f: A => Tree[B]): Tree[B] = {
    @tailrec
    def go(todo: List[Tree[A]], proc: Set[Tree[A]], done: List[Tree[B]]): Tree[B] = todo match {
      // traverse finished
      case Nil => done.head
      // Branch[A] - not visited => mark visited, dive deeper
      case (ha @ Branch(la, ra)) :: _ if !proc.contains(ha) =>
        go(ra :: la :: todo, proc + ha, done)
      // Branch[A] - already visited
      case (ha @ Branch(_, _)) :: t =>
        val newBranch = Branch(done.head, done.tail.head)
        go(t, proc - ha, newBranch :: done.tail.tail)
      // Leaf[A] - we can apply a function, and immediately put it to done
      case Leaf(a) :: t => go(t, proc, f(a) :: done)
    }

    go(List(t), Set.empty, List.empty)
  }

  def mapTreeTR[A, B](t: Tree[A])(f: A => B): Tree[B] =
    flatMapTreeTR(t)(a => Leaf(f(a)))
}
