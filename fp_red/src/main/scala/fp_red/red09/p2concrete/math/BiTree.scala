package fp_red.red09.p2concrete.math

object BiTree {

  /**
    * A - type of Value
    * B - any type wider than Expr[A]
    */
  sealed trait Expr[+A]
  final case class Value[A](x: A) extends Expr[A]
  final case class BiOp[A, EA >: Expr[A]](op: Char, l: EA, r: EA) extends Expr[A]

  def mkNode[A, EA >: Expr[A]](op: Char, n1: EA, n2: EA): EA = BiOp(op, n1, n2)
}
