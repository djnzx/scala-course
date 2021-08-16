package catsx.c119state

import cats.data.State
import cats.implicits.catsSyntaxApplicativeId
import cats.{Eval, Monoid}

import scala.annotation.tailrec

object C123ZPostOrderCalc extends App {
  type CalcBuffer = List[Int]
  type CalcState[A] = State[CalcBuffer, A]

  // eval number (push to list)
  def operand(num: Int): CalcState[Int] =
    State[CalcBuffer, Int] { old => (num :: old, num) }

  // eval operation (pop, pop, operation, push)
  def operator(f: (Int, Int) => Int): CalcState[Int] =
    State[CalcBuffer, Int] { old =>
      old match {
        case o1 :: o2 :: tail => {
          val r = f(o1, o2)
          (r :: tail, r)
        }
        case _ => sys.error("Parse Fail!")
      }
    }

  // eval one symbol: operation or operand
  def evalOne(sym: String): CalcState[Int] = sym match {
    case "+" => operator(_ + _)
    case "-" => operator(_ - _)
    case "*" => operator(_ * _)
    case "/" => operator(_ / _)
    case _ => operand(sym.toInt)
  }

  val test: CalcState[Int] = for {
    _ <- evalOne("1")
    _ <- evalOne("2")
    ans <- evalOne("+")
  } yield ans

  val userInputString = "1 2 + 3 * 1000 *"
  val userInput: List[String] = userInputString.split(" ").toList

  // initial empty state,
  // but actually we don't care about this value
  // look for the "_" in foldLeft
  // we will supply real user input further
  val initialState: CalcState[Int] = implicitly[Monoid[Int]].empty.pure[CalcState]

  // eval all user input, based on initialState
  def evalAll_it(input: List[String]): CalcState[Int] =
    input.foldLeft(initialState)((cs, s) => cs.flatMap(_ => evalOne(s)))

  @tailrec
  def evalAll_tr(input: List[String], state: CalcState[Int]): CalcState[Int] =
    input match {
      case Nil => state
      case h :: t => evalAll_tr(t, evalOne(h))
    }

  val test2: CalcState[Int] = for {
    _ <- evalAll_it(List("1", "2", "+"))
    _ <- evalAll_it(List("3", "4", "+"))
    ans <- evalOne("*")
  } yield ans

  // parse userInput to final transform function
  val parserIT: CalcState[Int] = evalAll_it(userInput)
  val parserTR: CalcState[Int] = evalAll_tr(userInput, initialState)

  // apply function built to Initial empty CalcBuffer (List[Int])
  val parsed: Eval[(CalcBuffer, Int)] = parserIT.run(Nil)
  val result: (List[Int], Int) = parsed.value
  println(result)
}
