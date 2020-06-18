package fp_red.red09.p2concrete.math

import fp_red.red09.p1impl.Reference
import fp_red.red09.p1impl.ReferenceTypes.Parser
import fp_red.red09.p2concrete.math.BiTree._

trait AbstractMathParser {
  val R = Reference
  import R._

  def process[A >: Expr](t: (A, Seq[(Char, A)])): A = t match {
    case (n, Nil) => n
    case (a, l) => l.foldLeft(a) { case (acc, (op, x)) => mkNode(op, acc, x) }
  }

  val mulOrDiv: Parser[Char] = char('*') | char('/')
  val plusOrMinus: Parser[Char] = char('+') | char('-')

  def value: Parser[Expr]

  /** recursive grammar */
  def parens: Parser[Expr] = surround(char('('), char(')'))(addSub)
  def factor: Parser[Expr] = value | parens

  def divMul: Parser[Expr] = ( factor ** (mulOrDiv ** factor).many ).map(process)
  def addSub: Parser[Expr] = ( divMul ** (plusOrMinus ** divMul).many ).map(process)

  /** root of grammar */
  def built: Parser[Expr] = root(addSub)

}
