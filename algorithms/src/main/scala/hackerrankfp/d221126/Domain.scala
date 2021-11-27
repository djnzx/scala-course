package hackerrankfp.d221126

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Domain {

  sealed trait Op
  sealed trait Op1 extends Op
  case object Plus extends Op1
  case object Minus extends Op1
  object Op1 {
    def unapply(s: String): Option[Op1] = s match {
      case "+" => Some(Plus)
      case "-" => Some(Minus)
      case _   => None
    }
    def unapply(c: Char): Option[Op1] = unapply(c.toString)
  }

  sealed trait Op2 extends Op
  case object Add extends Op2
  case object Sub extends Op2
  case object Mul extends Op2
  case object Div extends Op2
  object Op2 {
    def unapply(s: String): Option[Op2] = s match {
      case "+" => Some(Add)
      case "-" => Some(Sub)
      case "*" => Some(Mul)
      case "/" => Some(Div)
      case _   => None
    }
    def unapply(c: Char): Option[Op2] = unapply(c.toString)
  }

  sealed trait Expr
  final case class Value(x: Int) extends Expr
  final case class UnOp(op: Op1, e: Expr) extends Expr
  final case class Neg(ex: Expr) extends Expr
  final case class BinOp(op: Op2, l: Expr, r: Expr) extends Expr

  val MODULO: Int = 1000000007 // 10^9 + 7

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
        case Add => (evalNode(l) + evalNode(r)) % MODULO
        case Sub => evalNode(l) - evalNode(r)
        case Mul => (evalNode(l) * evalNode(r)) % MODULO
        case Div => evalNode(l) / evalNode(r)
      }
  }

}

class DomainSpec extends AnyFunSpec with Matchers {

  import Domain._

  def mapValid[A](xs: Seq[(A, Op)]) = xs.map { case (in, out) => (in, Some(out)) }
  def mapInvalid(xs: Seq[String]) = xs.map { in => (in, None) }

  it("Op1 - string based") {
    val valid: Seq[(String, Op1)] = Seq(
      "+" -> Plus,
      "-" -> Minus,
    )
    val invalid: Seq[String] = Seq(
      "",
      "z",
    )
    val testData = mapValid(valid) ++ mapInvalid(invalid)

    for {
      (in, out) <- testData
    } Op1.unapply(in) shouldEqual out
  }

  it("Op1 - char based") {
    val valid: Seq[(Char, Op1)] = Seq(
      '+' -> Plus,
      '-' -> Minus,
    )
    val testData = mapValid(valid)

    for {
      (in, out) <- testData
    } Op1.unapply(in) shouldEqual out
  }

  it("Op2") {
    val valid: Seq[(String, Op2)] = Seq(
      "+" -> Add,
      "-" -> Sub,
      "*" -> Mul,
      "/" -> Div,
    )
    val invalid: Seq[String] = Seq(
      "",
      "_",
    )
    val testData = mapValid(valid) ++ mapInvalid(invalid)

    for {
      (in, out) <- testData
    } Op2.unapply(in) shouldEqual out
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
