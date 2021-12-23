package tree

import Domain._
import scala.annotation.tailrec

object Implementations {

  def mapTree[A, B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(a)        => Leaf(f(a))
    case Branch(la, ra) => Branch(mapTree(la)(f), mapTree(ra)(f))
  }

  def mapTreeTR[A, B](t: Tree[A])(f: A => B): Tree[B] = {
    @tailrec
    def go(
        todo: List[Either[Tree[A], Tree[B]]],
        proc: Set[Either[Tree[A], Tree[B]]],
        done: List[Tree[B]],
      ): Tree[B] =
      if (todo.isEmpty) done.head
      else
        todo.head match {
          // Branch[A] - not visited
          case ta @ Left(Branch(la, ra)) if !proc.contains(ta) => go(Left(ra) :: Left(la) :: todo, proc + ta, done)
          // Branch[A] - already visited
          case ta @ Left(Branch(_, _)) =>
            val newL = done.head
            val newR = done.tail.head
            val newBr = Branch(newL, newR)
            go(todo.tail, proc - ta, newBr :: done.tail.tail)
          // Leaf[A] - we can apply a function, and immediately put it to done
          case Left(Leaf(a)) => go(todo.tail, proc, Leaf(f(a)) :: done)
          // Tree[B] - item is ready, tut them to done
          case Right(_) => throw new IllegalArgumentException("wrong state.")
        }

    go(List(Left(t)), Set.empty, List.empty)
  }
}
