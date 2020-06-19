package fp_red.red09.p2concrete.math_polynom

import fp_red.red09.algebra.MonoPolyNom.{Monom, Polynom}
import fp_red.red09.p1impl.ReferenceTypes.Parser
import fp_red.red09.p2concrete.math.BiTree.{Expr, Value}
import fp_red.red09.p2concrete.math._
import fp_red.red09.p2concrete.monom.MonomParser
import fp_red.red09.p2concrete.monom.MonomParser.NP

object MathOpToPolynomParser extends AbstractMathParser[Polynom] {
  import R.syntaxForParser
  
  override def value: Parser[Expr[Polynom]] = MonomParser.monom.map { case NP(n,p) => Value(Monom(n, p).toPolynom) }
}
