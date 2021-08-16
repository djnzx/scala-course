package catsx.c119state

import cats.data.State

object C123StateCalcExample extends App {

  sealed trait Cx
  final case class Num(v: Int) extends Cx
  final case class Op(name: String) extends Cx

  def parse(s: String): Option[Cx] =
    s.toIntOption.map(Num)
      .orElse(Option.when("+-*/".contains(s))(Op(s)))

  val entered = "1 2 + 3 *"
    .split(" ")
    .flatMap(parse)

  def doOp(a: Int, b: Int, op: String) = op match {
    case "+" => a + b
    case "-" => a - b
    case "*" => a * b
    case "/" => a / b
  }

  type CalcState[A] = State[List[Cx], A]

  def evalOne(sym: Cx): CalcState[Int] = ???

}
