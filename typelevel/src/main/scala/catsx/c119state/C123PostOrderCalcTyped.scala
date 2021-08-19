package catsx.c119state

import cats.data.State
import cats.implicits.catsSyntaxApplicativeId

import scala.annotation.tailrec

object C123PostOrderCalcTyped {

  sealed trait Cx
  final case class Num(v: Int) extends Cx
  final case class Op(name: String) extends Cx

  def parse(s: String): Option[Cx] =
    s.toIntOption.map(Num)
      .orElse(Option.when("+-*/".contains(s))(Op(s)))

  val input: List[Cx] = "1 2 + 3 *"
    .split(" ")
    .flatMap(parse)
    .toList

  def doOp(a: Int, b: Int, op: String) = op match {
    case "+" => a + b
    case "-" => a - b
    case "*" => a * b
    case "/" => a / b
  }

  type CalcStack = List[Cx]
  type CalcState[A] = State[CalcStack, A]

  def handleNumber(num: Int): CalcState[Int] =
    State[CalcStack, Int] { s => (Num(num) :: s, num) }

  def handleOperator(f: (Int, Int) => Int): CalcState[Int] =
    State[CalcStack, Int] {
      case Num(o1) :: Num(o2) :: tail =>
        val r = f(o1, o2)
        (Num(r) :: tail, r)
      case _ => sys.error("Parse Fail!")
    }

  def evalOne(sym: Cx): CalcState[Int] = sym match {
    case Op("+") => handleOperator(_ + _)
    case Op("-") => handleOperator(_ - _)
    case Op("*") => handleOperator(_ * _)
    case Op("/") => handleOperator(_ / _)
    case Num(v)  => handleNumber(v)
  }

  @tailrec
  def evalTailRecursive(input: List[Cx], fn: CalcState[Int] = 0.pure[CalcState]): CalcState[Int] =
    input match {
      case Nil => fn
      case sym :: t =>
        val step = fn.flatMap(_ => evalOne(sym))
        evalTailRecursive(t, step)
    }

}
