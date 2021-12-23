package tree

import Domain._
import scala.annotation.tailrec

object Implementations {

  /** stack recursive flatMap */
  def flatMapTree[A, B](t: Tree[A])(f: A => Tree[B]): Tree[B] = t match {
    case Leaf(a)        => f(a)
    case Branch(la, ra) => Branch(flatMapTree(la)(f), flatMapTree(ra)(f))
  }

  /** stack recursive map */
  def mapTree[A, B](t: Tree[A])(f: A => B): Tree[B] =
    flatMapTree(t)(a => Leaf(f(a)))

  /** tail recursive flatMap */
  def flatMapTreeTR[A, B](t: Tree[A])(f: A => Tree[B]): Tree[B] = {
    @tailrec
    def go(todo: List[Tree[A]], visited: Set[Tree[A]], done: List[Tree[B]]): Tree[B] = todo match {
      // traverse finished => take result from the done
      case Nil => done.head
      // Branch[A] - not visited => mark visited, dive deeper
      case (ha @ Branch(la, ra)) :: _ if !visited.contains(ha) =>
        go(ra :: la :: todo, visited + ha, done)
      // Branch[A] - already visited (on the back way) => remove from visited, reconstruct done
      case (ha @ Branch(_, _)) :: t =>
        done match {
          case bl :: br :: bt => go(t, visited - ha, Branch(bl, br) :: bt)
          case _              => throw new IllegalArgumentException("wrong state")
        }
      // Leaf[A] - we can apply a function, and immediately put it to done
      case Leaf(a) :: t => go(t, visited, f(a) :: done)
    }

    go(List(t), Set.empty, List.empty)
  }

  /** tail recursive map */
  def mapTreeTR[A, B](t: Tree[A])(f: A => B): Tree[B] =
    flatMapTreeTR(t)(a => Leaf(f(a)))

}
