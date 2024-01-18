package hackerrankfp.d211126

import hackerrankfp.d200612.Parsers
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object ExpressionParser {
  import hackerrankfp.d200612.Parsers._
  import Domain._
  import ParserImpl._

  /** fold sequence, right associative */
  def foldListRightAssoc(ex: Expr, ops: List[(Op2, Expr)]): Expr = ops match {
    case Nil            => ex
    case (op, ex2) :: t => BinOp(op, ex, foldListRightAssoc(ex2, t))
  }

  val foldRA: ((Expr, List[(Op2, Expr)])) => Expr = (foldListRightAssoc _).tupled

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

  /** implementation #2 */
  val plainValueWithUnary2: Parser[Expr] = plainValue | (unPlusOrMinus ** plainValue).map { case (op, ex) =>
    UnOp(op, ex)
  }

  /** implementation #3 */
  val plainValueWithUnary3: Parser[Expr] = (unPlusOrMinus.opt ** plainValue).map {
    case (Some(op), ex) => UnOp(op, ex)
    case (None, ex)     => ex
  }

  /** non recursive sequence of +, - */
  def plainLowPri: Parser[Expr] =
    (plainValueWithUnary3 ** (biPlusOrMinus ** plainValueWithUnary3).many) map foldRA

  /** non recursive sequence of *, / */
  def plainMidPri: Parser[Expr] =
    (plainValueWithUnary3 ** (biMulOrDiv ** plainValueWithUnary3).many) map foldRA

  /** parsing binary operation - high priority */
  val biMulOrDiv: Parser[Op2] = mulOrDiv.map(Op2.unapply).map(_.get)

  /** parsing binary operation - low priority */
  val biPlusOrMinus: Parser[Op2] = plusOrMinus.map(Op2.unapply).map(_.get)

  /** parsing whatever between parens */
  def parens(p: Parser[Expr]): Parser[Expr] = surround(char('('), char(')'))(p)

  /** treat everything wrapped into braces as unit */
  def hiPri: Parser[Expr] = plainValueWithUnary3 | parens(lowPri)

  /** MP - recursive */
  def midPri: Parser[Expr] =
    (hiPri ** (biMulOrDiv ** hiPri).many) map foldRA

  /** LP - recursive */
  def lowPri: Parser[Expr] =
    (midPri ** (biPlusOrMinus ** midPri).many) map foldRA

  def wholeCombination: Parser[Expr] = root(lowPri)

}

class ExpressionParserSpec extends AnyFunSpec with Matchers {

  import Evaluation._
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
      run(mayBeNeg)("") shouldEqual Right(None)
      run(mayBeNeg)("-") shouldEqual Right(Some('-'))
    }

  }

  describe("plain sequences") {
    it("plainLowPrio: +, -") {
      val p = run(plainLowPri)
      p("1+2-5-4").map(evalNode) shouldEqual Right(2)
    }

    it("plainMidPrio: *, /") {
      val p = run(plainMidPri)
      p("8/6/3").map(evalNode) shouldEqual Right(4)
      p("-8/-6/-3").map(evalNode) shouldEqual Right(-4)
    }
  }

  describe("recursive things") {
    it("1") {
      val p = run(wholeCombination)
      val r = p("(1+3-2)*8/4/-2")

      r shouldEqual Right(
        BinOp(
          op = Mul,
          l = BinOp(op = Add, l = Value(x = 1), r = BinOp(op = Sub, l = Value(x = 3), r = Value(x = 2))),
          r = BinOp(
            op = Div,
            l = Value(x = 8),
            r = BinOp(op = Div, l = Value(x = 4), r = UnOp(op = Minus, e = Value(x = 2))),
          ),
        ),
      )
    }
  }

}
