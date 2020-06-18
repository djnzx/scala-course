package fp_red.red09.p2concrete.math

object BiTree {
  // TODO make B as a type param for Expr
  sealed trait Expr
  final case class Value[B](x: B) extends Expr
  final case class BiOp[A >: Expr](op: Char, l: A, r: A) extends Expr

  def mkNode[A >: Expr](op: Char, n1: A, n2: A): A = BiOp(op, n1, n2)
}
