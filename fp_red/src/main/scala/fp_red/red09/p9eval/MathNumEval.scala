package fp_red.red09.p9eval

import fp_red.red09.p2concrete.math.BiTree._

object MathNumEval {

  def eval(root: Expr[Int]): Int = {

    def evalOp(op: Char, l: Expr[Int], r: Expr[Int]): Int = op match {
      case '+' => evalNode(l) + evalNode(r)
      case '-' => evalNode(l) - evalNode(r)
      case '*' => evalNode(l) * evalNode(r)
      case '/' => evalNode(l) / evalNode(r)
      case _   => ???
    }

    def evalNode(node: Expr[Int]): Int = node match {
      case Value(x)                             => x
      case BiOp(op, l: Expr[Int], r: Expr[Int]) => evalOp(op, l, r)
      case _                                    => ???
    }

    evalNode(root)
  }

}
