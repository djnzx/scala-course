package recursion

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object TreeApplication {

  import RecursionEvolution.step8.Functor

  /** binary tree application, A - type of node value */
  sealed trait Tree[+A]
  case class Node[A](left: Tree[A], value: A, right: Tree[A]) extends Tree[A]
  case object Leaf extends Tree[Nothing]

  /** functor pattern, A - is a Tree... */
  sealed trait TreeF[+T]
  case class NodeF[T, A](left: T, value: A, right: T) extends TreeF[T]
  case object LeafF extends TreeF[Nothing]

  def cata[A]: (
      Tree[A] => TreeF[(A, Tree[A])],
      TreeF[(A, Int)] => Int,
  ) => Tree[A] => Int =
    RecursionEvolution.step8.step87.cata[A, Int, TreeF, Tree]

  def cata0[A, R, F[_], S[_]](
      project: S[A] => F[S[A]],
      algebra: F[R] => R,
    )(
      implicit F: Functor[F],
    ): S[A] => R = xs => {

    def step(tail: S[A]): R = {
      val projected: F[S[A]] = project(tail)
      val fr: F[R] = F.map(projected) { z => step(z) }
      val r: R = algebra(fr)
      r
    }

    step(xs)
  }

  def cata0t[A]: Tree[A] => Int = cata0(projectTree[A], treeHeightAlgebra)

  implicit val ftf: Functor[TreeF] = new Functor[TreeF] {
    override def map[X, Y](ftf: TreeF[X])(f: X => Y): TreeF[Y] = ftf match {
      case LeafF          => LeafF
      case NodeF(l, v, r) => NodeF(f(l), v, f(r))
    }
  }

  /** projection for tree, how to decompose it */
  def projectTree[A]: Tree[A] => TreeF[Tree[A]] = {
    case Node(l, v, r) => NodeF(l, v, r)
    case Leaf          => LeafF
  }

  /** algebra for tree, how to count */
  def treeHeightAlgebra: TreeF[Int] => Int = {
    case LeafF          => 0
    case NodeF(l, _, r) => 1 + (l max r)
  }

  def height[A] = (t: Tree[A]) => cata0t[A](t)

}

class TreeApplicationSpec extends AnyFunSpec with Matchers {

  import TreeApplication.Leaf
  import TreeApplication.Node
  import TreeApplication.height

  describe("tree") {

    val tree0 = Leaf
    val tree1 = Node(Leaf, 22, Leaf)
    val tree2 = Node(Leaf, 22, Node(Leaf, 33, Leaf))
    val tree3 = Node(Leaf, 22, Node(Leaf, 33, Node(Leaf, 44, Leaf)))

    it("0") {
      height(tree0) shouldEqual 0
    }

    it("1") {
      height(tree1) shouldEqual 1
    }

    it("2") {
      height(tree2) shouldEqual 2
    }

    it("3") {
      height(tree3) shouldEqual 3
    }
  }

}
