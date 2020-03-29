package cats

object C127 extends App {

  sealed trait Tree[+A]
  final case class Leaf[A](value: A) extends Tree[A]
  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  def leaf[A](a: A): Tree[A] = Leaf(a)
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  val treeMonad: Monad[Tree] = new Monad[Tree] {
    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = ???

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = ???

    override def pure[A](x: A): Tree[A] = ???
  }
}
