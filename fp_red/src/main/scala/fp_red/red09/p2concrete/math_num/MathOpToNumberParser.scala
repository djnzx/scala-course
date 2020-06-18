package fp_red.red09.p2concrete.math_num

import fp_red.red09.p1impl.Reference
import fp_red.red09.p1impl.ReferenceTypes.Parser

object MathOpToNumberParser {
  val R = Reference
  import R._

  sealed trait Expr
  final case class Value(x: Int) extends Expr
  final case class BiOp[A >: Expr](op: Char, l: A, r: A) extends Expr

  def doOp[A >: Expr](op: Char, n1: A, n2: A): A = BiOp(op, n1, n2)

  def process[A >: Expr](t: (A, Seq[(Char, A)])): A = t match {
    case (n, Nil) => n
    case (a, l) => l.foldLeft(a) { case (acc, (op, x)) => doOp(op, acc, x) }
  }

  val mulOrDiv: Parser[Char] = char('*') | char('/')
  val plusOrMinus: Parser[Char] = char('+') | char('-')

  def number: Parser[Value] = integerWoSign.map(Value)

  /** recursive grammar */
  def parens: Parser[Expr] = surround(char('('), char(')'))(addSub)
  def factor: Parser[Expr] = number | parens

  def divMul: Parser[Expr] = ( factor ** (mulOrDiv ** factor).many ).map(process)
  def addSub: Parser[Expr] = ( divMul ** (plusOrMinus ** divMul).many ).map(process)

  /** root of grammar */
  def mathToExpr: Parser[Expr] = root(addSub)
}
