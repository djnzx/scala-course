package hackerrankfp.d221126

import hackerrankfp.d200612_10.Parsers
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object ExpressionParser {
  import hackerrankfp.d200612_10.Parsers._
  import Domain._
  import ParserImpl._

  def fold(xs: (Op2, List[(Char, Op2)])): Expr = ???

  /** plain chars */
  val plusOrMinus: Parser[Char] = char('+') | char('-')
  val mulOrDiv: Parser[Char] = char('*') | char('/')
  val plainValue: Parser[Value] = number.map(x => Value(x))
  val plainValueWithPlus: Parser[UnOp] = (char('+') *> plainValue) map { UnOp(Plus, _) }
  val plainValueWithMinus: Parser[UnOp] = (char('-') *> plainValue) map { UnOp(Minus, _) }

  /** plain implementation #1 */
  val plainValueWithUnary1: Parser[Expr] = plainValue | plainValueWithPlus | plainValueWithMinus
  // FIXME: implement via Parser.flatMap
  val unPlusOrMinus: Parser[Op1] = plusOrMinus.map(Op1.unapply).map(_.get)

  /** plain implementation #1 */
  val plainValueWithUnary2: Parser[Expr] = plainValue | (unPlusOrMinus ** plainValue)
    .map { case (op, ex) =>
      UnOp(op, ex)
    }

  val plainValueWithUnary3: Parser[Expr] = (unPlusOrMinus.opt ** plainValue)
    .map {
      case (Some(op), ex) => UnOp(op, ex)
      case (None, ex)     => ex
    }

  val biPlusOrMinus: Parser[Op2] = plusOrMinus.map(Op2.unapply).map(_.get)
  val biMulOrDiv: Parser[Op2] = mulOrDiv.map(Op2.unapply).map(_.get)

  def parens: Parser[Expr] = surround(char('('), char(')'))(addSub)
  def block: Parser[Expr] = plainValue | parens
  def addSub: Parser[Expr] = (biMulOrDiv ** (plusOrMinus ** biMulOrDiv).many).map(fold)
  def wholeCombination: Parser[Expr] = root(addSub)
}

class ExpressionParserSpec extends AnyFunSpec with Matchers {

  import Domain._
  import Parsers._
  import ParserImpl._
  import ExpressionParser._
  def run[A](p: Parser[A]): String => Either[Parsers.ParseError, A] = ParserImpl.run(p)

  it("plain number as Value") {
    val intParser = run(plainValue)
    intParser("123") shouldEqual Right(Value(123))
  }

  describe("unary lifted") {
    val p = run(plainValueWithUnary3)

    it("plain") {
      p("123") shouldEqual Right(Value(123))
    }

    it("lifted to -") {
      p("-123") shouldEqual Right(UnOp(Minus, Value(123)))
    }

    it("lifted to +") {
      p("+123") shouldEqual Right(UnOp(Plus, Value(123)))
    }

    it("wrong") {
      p("_123").isLeft shouldEqual true
      p("").isLeft shouldEqual true
    }

    it("opt - nonOpt") {
      val mayBeNeg: Parser[Option[Char]] = char('-').opt
      val mayBeNegNonOpt: Parser[Char] = mayBeNeg.nonOpt

      run(mayBeNeg)("") shouldEqual Right(None)
      run(mayBeNeg)("-") shouldEqual Right(Some('-'))
    }

  }

}
