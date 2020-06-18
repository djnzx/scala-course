package fp_red.red09.p2concrete.math_num

import fp_red.red09.p1impl.ReferenceTypes.Parser
import fp_red.red09.p2concrete.math.BiTree._
import fp_red.red09.p2concrete.math._

object MathOpToNumberParser extends AbstractMathParser {
  import R.{integerWoSign, syntaxForParser}

  override def value: Parser[Expr] = integerWoSign.map(x => Value(x))
}
