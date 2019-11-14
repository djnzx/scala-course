package x060essential

/**
  * algebraic datatype
  * abstract syntax tree
  */
object X159CalcV4 extends App {
  sealed trait YEither[+L, +R] {
    def fold[C](fl: L => C)(fr: R => C): C = this match {
      case Failure(l) => fl(l)
      case Success(r) => fr(r)
    }
    def map[R2](fn: R => R2): YEither[L, R2] = this match {
      case Failure(l) => Failure(l)
      case Success(r) => Success(fn(r))
    }
    // we need to open generic only here to preserve the real result type of failure
    def flatMap[LL >: L, R2](fn: R => YEither[LL, R2]): YEither[LL, R2] = this match {
      case Failure(l) => Failure(l)
      case Success(r) => fn(r)
    }
  }
  final case class Failure[String](reason: String) extends YEither[String, Nothing]
  final case class Success[Double](result: Double) extends YEither[Nothing, Double]

  sealed trait Expression {
    def lift2(l: Expression, r: Expression, f: (Double, Double) => YEither[String, Double]): YEither[String, Double] =
      l.eval.flatMap { left => r.eval.flatMap { right => f(left, right) }}
    def lift1(e: Expression, f: Double => YEither[String, Double]): YEither[String, Double] =
      e.eval.flatMap { value => f(value) }
    def eval: YEither[String, Double] = this match {
      case Number(value) => Success(value)
      case Addition(l, r) => lift2(l, r, (a, b) => Success(a + b))
      case Subtraction(l, r) => lift2(l, r, (a, b) => Success(a - b))
      case Multiplication(l, r) => lift2(l, r, (a, b) => Success(a * b))
      case Division(l, r) => lift2(l, r, (a, b) => if (b!=0) Success(a / b) else Failure("Division by zero"))
      case SquareRoot(va) => lift1(va, v => if (v>=0) Success(Math.sqrt(v)) else Failure("Square root of negative"))
    }
  }
  final case class Number(value: Double) extends Expression
  final case class NumberOpt(value: Option[Double]) extends Expression
  final case class Addition(left: Expression, right: Expression) extends Expression
  final case class Subtraction(left: Expression, right: Expression) extends Expression
  final case class Multiplication(left: Expression, right: Expression) extends Expression
  final case class Division(numerator: Expression, denominator: Expression) extends Expression
  final case class SquareRoot(value: Expression) extends Expression

  assert(Addition(SquareRoot(Number(-1.0)), Number(2.0)).eval == Failure("Square root of negative"))
  assert(Addition(SquareRoot(Number(4.0)), Number(2.0)).eval == Success (4.0))
  assert(Division(Number(4), Number(0)).eval == Failure("Division by zero"))

  val a: Expression = Addition(Subtraction(Number(6), Number(3)), Number(3))
  val r: YEither[String, Double] = a.eval
  println(r)
}
