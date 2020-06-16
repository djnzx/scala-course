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
}
