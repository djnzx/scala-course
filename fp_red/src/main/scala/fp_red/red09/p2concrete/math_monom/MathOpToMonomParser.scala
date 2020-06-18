package fp_red.red09.p2concrete.math_monom

import fp_red.red09.algebra.MonoPolyNom.Monom
import fp_red.red09.p1impl.Reference
import fp_red.red09.p1impl.ReferenceTypes.Parser
import fp_red.red09.p2concrete.monom.MonomParser
import fp_red.red09.p2concrete.monom.MonomParser.NP

object MathOpToMonomParser {
  val R = Reference
  import R._

  sealed trait Expr
  final case class Value(x: Monom) extends Expr
  final case class BiOp[A >: Expr](op: Char, l: A, r: A) extends Expr

  def doOp[A >: Expr](op: Char, n1: A, n2: A): A = BiOp(op, n1, n2)

  def process[A >: Expr](t: (A, Seq[(Char, A)])): A = t match {
    case (n, Nil) => n
    case (a, l) => l.foldLeft(a) { case (acc, (op, x)) => doOp(op, acc, x) }
  }

  val mulOrDiv: Parser[Char] = char('*') | char('/')
  val plusOrMinus: Parser[Char] = char('+') | char('-')

  def monom: Parser[Expr] = MonomParser.monom.map { case NP(n,p) => Value(Monom(n, p)) }

  /** recursive grammar */
  def parens: Parser[Expr] = surround(char('('), char(')'))(addSub)
  def factor: Parser[Expr] = monom | parens

  def divMul: Parser[Expr] = ( factor ** (mulOrDiv ** factor).many ).map(process)
  def addSub: Parser[Expr] = ( divMul ** (plusOrMinus ** divMul).many ).map(process)

  /** root of grammar */
  def mathToMonom: Parser[Expr] = root(addSub)
}
