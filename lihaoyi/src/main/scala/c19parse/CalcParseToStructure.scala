package c19parse

import fastparse.NoWhitespace._
import fastparse._

object CalcParseToStructure {

  sealed trait Expr
  final case class Value(x: Int) extends Expr
  final case class BiOp(op: String, l: Expr, r: Expr) extends Expr

  def doOp(op: String, n1: Expr, n2: Expr): BiOp = BiOp(op, n1, n2)
  /** 
    * just evaluate
    * t._1 n1
    * t._2 Seq(op, n2)
    */
  def process(t: (Expr, Seq[(String, Expr)])): Expr = t match {
    case (n, Nil) => n
    case (a, l) => l.foldLeft(a) { case (acc, (op, x)) => doOp(op, acc, x) }
  }
  /** extract number */
  def number[_: P]: P[Value] = P( CharIn("0-9").rep(1).!.map(_.toInt).map(Value) )
  
  
  /** recursive grammar */
  def parens[_: P]: P[Expr] = P( "(" ~/ addSub ~ ")" )
  def factor[_: P]: P[Expr] = P( number | parens )
  def divMul[_: P]: P[Expr] = P( factor ~ (CharIn("*/").! ~/ factor).rep ).map(process)
  def addSub[_: P]: P[Expr] = P( divMul ~ (CharIn("+\\-").! ~/ divMul).rep ).map(process)
  
  /** root of grammar */
  def build[_: P] = P( addSub ~ End )
  
}
