package catsx.c119state

import cats.data.State
import cats.implicits.catsSyntaxApplicativeId

import scala.annotation.tailrec

object C123PostOrderCalc extends App {

  /** type aliases */
  type CalcStack = List[Int]
  type CalcState[A] = State[CalcStack, A]

  /** eval number (push to list) */
  def handleNumber(num: Int): CalcState[Int] =
    State[CalcStack, Int] { s => (num :: s, num) }

  /** eval operation (pop, pop, operation, push) */
  def handleOperator(f: (Int, Int) => Int): CalcState[Int] =
    State[CalcStack, Int] {
      case o1 :: o2 :: tail =>
        val r = f(o1, o2)
        (r :: tail, r)
      case _ => sys.error("Parse Fail!")
    }

  /** eval one symbol */
  def evalOne(sym: String): CalcState[Int] = sym match {
    case "+" => handleOperator(_ + _)
    case "-" => handleOperator(_ - _)
    case "*" => handleOperator(_ * _)
    case "/" => handleOperator(_ / _)
    case _   => handleNumber(sym.toInt)
  }

  /** iterative fold */
  def evalIterative(input: List[String]): CalcState[Int] =
    input.foldLeft(0.pure[CalcState]) { (fn, sym) =>
      fn.flatMap(_ => evalOne(sym))
    }

  /** tail recursive fold */
  @tailrec
  def evalTailRecursive(input: List[String], fn: CalcState[Int] = 0.pure[CalcState]): CalcState[Int] =
    input match {
      case Nil => fn
      case sym :: t =>
        val step = fn.flatMap(_ => evalOne(sym))
        evalTailRecursive(t, step)
    }

}
