package hackerrankfp.d211126_11

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Evaluation {

  import Domain._
  import Euclidean._

  val PRIME: Int = 1000000007 // 10^9 + 7

  implicit class RichInt(private val i: Int) extends AnyVal {
    def emod: Int = i %% PRIME
  }

  /** standard things */
  def modAdd(a: Int, b: Int): Int = (a.emod + b.emod).emod
  def modSub(a: Int, b: Int): Int = (a.emod - b.emod).emod
  def modMul(a: Int, b: Int): Int = (a.emod * b.emod).emod

  /** hacky thing */
  def modDiv(a: Int, b: Int): Int =
    if (a % b == 0) a / b
    else modMul(a, modDivide(1, b, PRIME))

  def evalNode(node: Expr): Int = node match {
    case Value(x) => x
    case Neg(ex)  => -evalNode(ex)
    case UnOp(op, ex: Expr) =>
      op match {
        case Minus => -evalNode(ex)
        case Plus  => +evalNode(ex)
      }
    case BinOp(op, l: Expr, r: Expr) =>
      op match {
        case Add => modAdd(evalNode(l), evalNode(r))
        case Sub => modSub(evalNode(l), evalNode(r))
        case Mul => modMul(evalNode(l), evalNode(r))
        case Div => modDiv(evalNode(l), evalNode(r))
      }
  }

}

class EvaluationSpec extends AnyFunSpec with Matchers {

  import Domain._
  import Evaluation._

  it("01") {
    modDiv(1, 10) shouldEqual 700000005
  }

  it("02") {
    modMul(2, 700000005) shouldEqual 400000003
  }

  it("03") {
    modMul(1, -400000003) shouldEqual 600000004
  }

  it("04") {
    modDiv(1, 600000004) shouldEqual 1000000002
  }

  it("05") {
    modMul(4, 1000000002) shouldEqual 999999987
  }

  it("eval") {
    val ex = BinOp(
      Add,
      Neg(Value(3)),
      BinOp(Mul, Value(2), Value(4)),
    )

    evalNode(ex) shouldEqual 5
  }

}
