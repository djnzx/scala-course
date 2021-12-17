package fp_red.red09.p9eval

import fp_red.red09.algebra.MonoPolyNom.Polynom
import fp_red.red09.p2concrete.math.BiTree._

object MathPolynomEval {

  def eval(root: Expr[Polynom]): Polynom = {

    def evalOp(op: Char, l: Expr[Polynom], r: Expr[Polynom]): Polynom = op match {
      case '+' => evalNode(l) + evalNode(r)
      case '-' => evalNode(l) - evalNode(r)
      case '*' => evalNode(l) * evalNode(r)
      case '/' => evalNode(l) / evalNode(r)
    }

    def evalNode(node: Expr[Polynom]): Polynom = node match {
      case Value(x)                                     => x
      case BiOp(op, l: Expr[Polynom], r: Expr[Polynom]) => evalOp(op, l, r)
      case _                                            => ???
    }

    evalNode(root)

  }

}
