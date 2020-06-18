package fp_red.red09.p2concrete.math

object BiTree {

  /**
    * A - type of Value
    * B - any type wider than Expr[A]
    */
  sealed trait Expr[+A]
  final case class Value[A](x: A) extends Expr[A]
  final case class BiOp[A, B >: Expr[A]](op: Char, l: B, r: B) extends Expr[A]

  def mkNode[A, B >: Expr[A]](op: Char, n1: B, n2: B): B = BiOp(op, n1, n2)
}
