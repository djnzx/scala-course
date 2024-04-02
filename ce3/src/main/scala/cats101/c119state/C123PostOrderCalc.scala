package cats101.c119state

import cats.Monoid
import cats.data.State
import cats.implicits.catsSyntaxApplicativeId
import cats101.c119state.C123PostOrderCalc.{CalcStack, CalcState, evalIterative, evalOne, evalTailRecursive}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec

object C123PostOrderCalc {

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

class C123PostOrderCalcSpec extends AnyFunSpec with Matchers {

  describe("basic stuff") {

    it("eval one number to a state") {
      val state = List(1, 2, 3)
      val fn: CalcState[Int] = evalOne("100")
      val state2: List[Int] = fn.runS(state).value
      state2 shouldEqual List(100, 1, 2, 3)
    }

    it("eval one number to a value") {
      val state = List(1, 2, 3)
      val fn: CalcState[Int] = evalOne("100")
      val value2: Int = fn.runA(state).value
      value2 shouldEqual 100
    }

    it("eval operator +") {
      val state = List(10, 2)
      val fn = evalOne("+")
      val state2: List[Int] = fn.runS(state).value
      state2 shouldEqual List(12)
    }

    it("eval operator *") {
      val state = List(10, 2)
      val fn = evalOne("*")
      val state2: List[Int] = fn.runS(state).value
      state2 shouldEqual List(20)
    }

    it("manual combination #1") {
      val fn: CalcState[Int] = for {
        _ <- evalOne("1")
        _ <- evalOne("2")
        ans <- evalOne("+")
      } yield ans
      fn.runS(List.empty).value shouldEqual List(3)
    }

  }

  describe("composition") {

    /** possible ways to declare "empty" state function */
    val emptyState: CalcState[Int] = 0.pure[CalcState]
    val emptyState1: CalcState[Int] = implicitly[Monoid[Int]].empty.pure[CalcState]
    val emptyState2: CalcState[Int] = State[CalcStack, Int] { s =>
      (s, 0)
    }

    val userInput = "1 2 + 3 * 1000 *"
      .split(" ")
      .toList

    val expectedV = 9000
    val expectedS = List(expectedV)

    it("manual combination #2") {
      val fn: CalcState[Int] = for {
        _ <- evalIterative(List("1", "2", "+"))
        _ <- evalIterative(List("3", "4", "+"))
        r <- evalOne("*")
      } yield r
      fn.runS(List.empty).value shouldEqual List(21)
    }

    it("iterative eval, non-intuitive") {
      evalIterative(userInput).runS(List.empty).value shouldEqual expectedS
    }

    it("tail recursive eval") {
      val fn: CalcState[Int] = evalTailRecursive(userInput)
      fn.run(Nil).value shouldEqual (expectedS, expectedV)
    }

  }

}
