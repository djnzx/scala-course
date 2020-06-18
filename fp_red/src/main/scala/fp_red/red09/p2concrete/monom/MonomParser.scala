package fp_red.red09.p2concrete.monom

import fp_red.red09.p1impl.Reference
import fp_red.red09.p1impl.ReferenceTypes.Parser

object MonomParser {
  val R = Reference
  import R._
  case class NP(n: Int, p: Int)

  val n: Parser[Int] = integerWoSign
  val x: Parser[Char] = char('x')
  val p: Parser[Int] = char('^') *> integerWoSign

  // nx^p
  val nxp: Parser[NP] = (n <* x) ** p map { case (n, p) => NP(n, p) } label "P1:"
  // nx
  val nx1: Parser[NP] = n <* x map { NP(_, 1) } label "P2:"
  // n
  val n_ : Parser[NP] = n map { NP(_, 0) } label "P3:"
  // x^p
  val xp : Parser[NP] = x *> p map { NP(1, _) } label "P4:"
  // x
  val x_ : Parser[NP] = x *> R.succeed(NP(1, 1)) label "P5:"
  // combine everything
  val monom: Parser[NP] = attempt(nxp) | attempt(nx1) | attempt(n_) | attempt(xp) | attempt(x_)
}
