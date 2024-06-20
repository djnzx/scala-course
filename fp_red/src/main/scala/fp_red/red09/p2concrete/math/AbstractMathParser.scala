package fp_red.red09.p2concrete.math

import fp_red.red09.p1impl.Reference
import fp_red.red09.p1impl.ReferenceTypes.Parser
import fp_red.red09.p2concrete.math.BiTree._

trait AbstractMathParser[A] {
  val R = Reference
  import R._

  def combine[EA >: Expr[A]](t: (EA, Seq[(Char, EA)])): EA = t match {
    case (n, Nil) => n
    case (a, l)   => l.foldLeft(a) { case (acc, (op, x)) => mkNode(op, acc, x) }
  }

  val `+|-` : Parser[Char] = char('+') | char('-')
  val `*|/` : Parser[Char] = char('*') | char('/')

  /** this is what we call value: integer.map(x => Value(x)) */
  def value: Parser[Expr[A]]

  /** whatever between parens */
  def parens: Parser[Expr[A]] = surround(char('('), char(')'))(addSubSeq)

  def term: Parser[Expr[A]] = value | parens

  /** recursive grammar */
  def mulDivSeq: Parser[Expr[A]] = (term ** (`*|/` ** term).many) // (Expr[A], List[(Char, Expr[A])])
    .map(combine)

  def addSubSeq: Parser[Expr[A]] = (mulDivSeq ** (`+|-` ** mulDivSeq).many) // (Expr[A], List[(Char, Expr[A])])
    .map(combine)

  /** root of grammar */
  def built: Parser[Expr[A]] = root(addSubSeq)

}
