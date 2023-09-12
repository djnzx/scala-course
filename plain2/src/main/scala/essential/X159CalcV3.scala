package essential

/**
  * algebraic datatype
  * abstract syntax tree
  */
object X159CalcV3 extends App {
  sealed trait YEither[+L, +R] {
    def fold[LL >: L, RR >: R, C](fl: LL => C)(fr: RR => C): C = this match {
      case Failure(l) => fl(l)
      case Success(r) => fr(r)
    }
    def map   [LL >: L, RR >: R, R2](fn: RR => R2):YEither[LL, R2] = this match {
      case Failure(l) => Failure(l)
      case Success(r) => Success(fn(r))
    }
    def flatMap[LL >: L, RR >: R, R2](fn: RR => YEither[LL, R2]):YEither[LL, R2] = this match {
      case Failure(l) => Failure[LL](l)
      case Success(r) => fn(r)
    }
  }
  final case class Failure[S1](reason: S1) extends YEither[S1, Nothing]
  final case class Success[D1](result: D1) extends YEither[Nothing, D1]

  sealed trait Expression {
    def eval: YEither[String, Double] = this match {
      case Number(value) => Success(value)
      case Addition(l, r) => l.eval match {
        case Failure(f) => Failure(f)
        case Success(l1) => r.eval match {
          case Failure(f) => Failure(f)
          case Success(r2) => Success(l1 + r2)
        }
      }
      case Subtraction(l, r) => l.eval match {
        case Failure(f) => Failure(f)
        case Success(l1) => r.eval match {
          case Failure(f) => Failure(f)
          case Success(r2) => Success(l1 - r2)
        }
      }
      case Multiplication(l, r) => l.eval match {
        case Failure(f) => Failure(f)
        case Success(l1) => r.eval match {
          case Failure(f) => Failure(f)
          case Success(r2) => Success(l1 * r2)
        }
      }
      case Division(n, d) => n.eval match {
        case Failure(f) => Failure(f)
        case Success(n1) => d.eval match {
          case Failure(f) => Failure(f)
          case Success(d2) => if (d2 == 0) Failure("Division by zero")
                              else         Success(n1 / d2)
        }
      }
      case SquareRoot(v) => v.eval match {
        case Failure(f) => Failure(f)
        case Success(v1) => if (v1 < 0) Failure("Square root of negative")
                            else        Success(Math.sqrt(v1))
      }
    }
  }
  final case class Number(value: Double) extends Expression
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
