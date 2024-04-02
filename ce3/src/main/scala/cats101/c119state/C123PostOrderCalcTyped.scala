package cats101.c119state

import cats._
import cats.data._
import cats.implicits._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import scala.annotation.tailrec

object C123PostOrderCalcTyped {

  sealed trait Sym extends Product with Serializable
  object Sym {
    final case class Num(v: Int) extends Sym
    sealed trait Op              extends Sym {
      def apply = (a: Int, b: Int) =>
        this match {
          case Op.Plus  => a + b
          case Op.Minus => a - b
          case Op.Mul   => a * b
          case Op.Div   => a / b
        }
    }
    object Op {
      case object Plus  extends Op
      case object Minus extends Op
      case object Mul   extends Op
      case object Div   extends Op
      private val applyPartial: PartialFunction[String, Op] = {
        case "+" => Plus
        case "-" => Minus
        case "*" => Mul
        case "/" => Div
      }
      def apply = applyPartial.lift
    }
    def parse(s: String): Option[Sym] =
      s.toIntOption
        .map(Num)
        .orElse(Op.apply(s))
  }
  import Sym.Num

  type CalcStack = List[Sym]
  type CalcState[A] = State[CalcStack, A]

  /** handling Number (pushing to stack) lifted to State */
  def handleNum(num: Int): CalcState[Int] = State[CalcStack, Int](s => (Num(num) :: s, num))

  /** handling Operation lifted to State */
  def handleOp(f: Sym.Op): CalcState[Int] = State[CalcStack, Int] {
    case Num(n1) :: Num(n2) :: xs =>
      val r = f.apply(n1, n2)
      (Num(r) :: xs, r)
    case _                        => sys.error("Parse Fail!")
  }

  def handle(sym: Sym): CalcState[Int] = sym match {
    case Num(v)     => handleNum(v)
    case op: Sym.Op => handleOp(op)
  }

  def doEval(xs: List[Sym]): CalcState[Int] => CalcState[Int] = {

    @tailrec
    def go(xs: List[Sym], state: CalcState[Int]): CalcState[Int] = xs match {
      case Nil     => state
      case s :: ss =>
        val step = state.flatMap(_ => handle(s))
        go(ss, step)
    }

    (state: CalcState[Int]) => go(xs, state)
  }

  def eval(in: String, buf: CalcStack = Nil): Int = {

    // parse
    val parsed: List[Sym] = in
      .split(" ")
      .flatMap(Sym.parse)
      .toList

    /** build a computation by analyzing stack.
      * since there is no was to provide initial state,
      * result is a function: State => State
      */
    val state: CalcState[Int] => CalcState[Int] = doEval(parsed)

    /** since `CalcState = State[CalcStack, A]`,
      *
      * we need to provide initial `CalcStack`
      */
    val stateInitial: CalcState[Int] = 0.pure[CalcState]

    /** final state after applying whole composition */
    val stateFinal: CalcState[Int] = state.apply(stateInitial)

    /** providing initial call stack */
    val eval: Eval[Int] = stateFinal.runA(buf)

    /** actually run Eval Monad */
    val value = eval.value
    value
  }

}

class C123PostOrderCalcTypedSpec extends AnyFunSpec with Matchers {
  import C123PostOrderCalcTyped.Sym.Num
  import C123PostOrderCalcTyped._

  describe("eval") {

    it("eval with empty buffer") {
      eval("1 2 + 3 *") shouldBe 9
    }

    it("eval with non-empty buffer") {
      eval("1 2 + 3 * *", List(Num(100))) shouldBe 900
    }

  }

}
