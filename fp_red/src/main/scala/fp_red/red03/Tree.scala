package fp_red.red03

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
object Tr {
  def leaf[A](a: A): Tree[A] = Leaf(a)
  def branch[A](l: Tree[A], r: Tree[A]): Tree[A] = Branch(l, r) 
}

object Tree {

  def size[A](t: Tree[A]): Int = t match {
    case Leaf(_) => 1
    case Branch(l, r) => size(l) + size(r) + 1 // it depends on req. whether co count leafs only
  }

  def maximum(t: Tree[Int]): Int = t match {
    case Leaf(value) => value
    case Branch(left, right) => maximum(left) max maximum(right)
  }
  
  def depth[A](t: Tree[A]): Int = t match {
    case Leaf(_) => 0
    case Branch(left, right) => 1 + (depth(left) max depth(right))
  }
  
  private def depth2[A](t: Tree[A], d: Int): Int = t match {
    case Leaf(_) => d
    case Branch(left, right) => depth2(left, d + 1) max depth2(right, d + 1)
  }
  
  def depth2[A](t: Tree[A]): Int = depth2(t, 1)
  
  def map[A,B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(a) => Leaf(f(a))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }

  /**
    * @param t - tree
    * @param f - Leaf handler
    * @param g - Branch handler
    */
  def fold[A,B](t: Tree[A])(f: A => B)(g: (B, B) => B): B = t match {
    case Leaf(value) => f(value)
    case Branch(left, right) => 
      g(
        fold(left)(f)(g), 
        fold(right)(f)(g)
      )
  }
  
  def sizeViaFold[A](t: Tree[A]): Int = 
    fold(t) { _ => 1 } { (ls, rs) => ls + rs + 1 }

  def sizeViaFold2[A](t: Tree[A]): Int =
    fold(t) { _ => 1 } { _ + _ + 1 }

  def sumViaFold(t: Tree[Int]): Int =
    fold(t) { identity } { _ + _ }

  def maxViaFold(t: Tree[Int]): Int =
    fold(t) { identity } { _ max _ }

  def depthViaFold[A](t: Tree[A]): Int =
    fold(t) { _ => 0 } { (d1, d2) => 1 + (d1 max d2)}

  def mapViaFold[A,B](t: Tree[A])(f: A => B): Tree[B] =
    fold(t) { a => Leaf(f(a)): Tree[B] } { Branch(_, _) }

  def mapViaFold2[A,B](t: Tree[A])(f: A => B): Tree[B] =
    fold(t) { a => Tr.leaf(f(a)) } { Branch(_, _) }

}
