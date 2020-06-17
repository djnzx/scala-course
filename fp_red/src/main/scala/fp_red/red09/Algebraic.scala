package fp_red.red09

import fp_red.red09.ReferenceTypes.Parser

object Algebraic {
  val R = Reference
  import R._

  object MonomParser {
    case class NP(n: Int, p: Int)

    val n = integerWoSign
    val x = char('x')
    val p = char('^') *> integerWoSign

    val nxp: Parser[NP] = (n <* x) ** p map { case (n, p) => NP(n, p) } label "P1:"
    val nx1: Parser[NP] = n <* x map { NP(_, 1) } label "P2:"
    val n_ : Parser[NP] = n map { NP(_, 0) } label "P3:"
    val xp : Parser[NP] = x *> p map { NP(1, _) } label "P4:"
    val x_ : Parser[NP] = x *> R.succeed(NP(1, 1)) label "P5:"
    val monom: Parser[NP] = attempt(nxp) | attempt(nx1) |
      attempt(n_) | attempt(xp) | attempt(x_)

  }
  
  object CalcParser {
    sealed trait Expr
    final case class Value(x: Int) extends Expr
    final case class BiOp(op: Char, l: Expr, r: Expr) extends Expr
    
    def doOp(op: Char, n1: Expr, n2: Expr): BiOp = BiOp(op, n1, n2)

    def process(t: (Expr, Seq[(Char, Expr)])): Expr = t match {
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
    def build: Parser[Expr] = root(addSub)
  }
}
