package catsparse

import cats.parse.Numbers
import cats.parse.Parser
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** https://github.com/typelevel/cats-parse */
class CalculatorSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  sealed trait Expr[+A]
  final case class Value[A](x: A)                            extends Expr[A]
  final case class BiOp[A](op: Char, l: Expr[A], r: Expr[A]) extends Expr[A]

  def combine(t: (Expr[Int], List[(Char, Expr[Int])])): Expr[Int] = t match {
    case (n, Nil) => n
    case (a, l)   => l.foldLeft(a) { case (acc, (op, x)) => BiOp(op, acc, x) }
  }

  val plusMinus = Parser.charIn('+', '-')
  val mulDiv = Parser.charIn('*', '/')

  val valueP = Numbers.digits.map(_.toInt).map(Value.apply)

  def parens = Parser.defer(addSubSeq.between(Parser.char('('), Parser.char(')')))

  def term = valueP | parens

  def mulDivSeq: Parser[Expr[Int]] = (term ~ (mulDiv ~ term).rep0).map(combine)

  def addSubSeq = (mulDivSeq ~ (plusMinus ~ mulDivSeq).rep0).map(combine)

  def full = addSubSeq <* Parser.end

  def eval(e: Expr[Int]): Int = e match {
    case Value(x)        => x
    case BiOp('+', l, r) => eval(l) + eval(r)
    case BiOp('-', l, r) => eval(l) - eval(r)
    case BiOp('*', l, r) => eval(l) * eval(r)
    case BiOp('/', l, r) => eval(l) / eval(r)
    case _               => sys.error("impossible by design")
  }

  test("calculator") {
    val x = full.parseAll("(1+2)*(6-3)").getOrElse(???)
    pprint.log(x)
    pprint.log(eval(x))
  }

}
