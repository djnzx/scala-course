package cats

object C206UseCase10ValidatedPredicate extends App {
  import cats.data.Validated
  import cats.data.Validated.{Invalid, Valid}
  import cats.syntax.apply._
  import cats.syntax.semigroup._ // |+|

  type CheckFn[E, A] = A => Validated[E, A]

  sealed trait Predicate[E, A] {
    def apply(a: A)(implicit ev: Semigroup[E]): Validated[E, A] = this match {
      case PPure(f) => f(a)
      case And(lf, rf) => (lf(a), rf(a)).mapN((_, _) => a)
      case Or(lf, rf) => (lf(a), rf(a)) match {
        case (Invalid(e1), Invalid(e2)) => Invalid(e1 |+| e2)
        case _ => Valid(a)
      }
    }

    def and(that: Predicate[E, A]): Predicate[E, A] = And(this, that)
    def or(that: Predicate[E, A]): Predicate[E, A] = Or(this, that)
  }
  final case class And[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
  final case class Or[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
  final case class PPure[E, A](f: CheckFn[E, A]) extends Predicate[E, A]

  sealed trait Check[E, A, B] {
    def apply(a: A)(implicit ev: Semigroup[E]): Validated[E, B]
    def map[C](f: B => C): Check[E, A, C] = Map[E, A, B, C](this, f)
  }
  object Check {
    def apply[E, A](p: Predicate[E, A]): Check[E, A, A] = Pure(p)
  }
  final case class Map[E,A,B,C](check: Check[E, A, B], f: B => C) extends Check[E, A, C] {
    def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, C] = check(in).map(f)
  }
  final case class Pure[E, A](p: Predicate[E, A]) extends Check[E, A, A] {
    def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, A] = p(in)
  }

  def test(): Unit = {
//    val gt10 =
//      Pure { x: Int => if (x>10) Valid(x) else Invalid(List("not gt10")) }
//    val gt20 =
//      Pure { x: Int => if (x>20) Valid(x) else Invalid(List("not gt20")) }
//    val noFail =
//      Pure { x: Int => Valid(x) }
//
//    val gt1020 =
//      noFail and gt10 and gt20
//
//    println(s"And: ${gt1020(5)}")  // Invalid(List(not gt10, not gt20))
//    println(s"And: ${gt1020(15)}") // Invalid(List(not gt20))
//    println(s"And: ${gt1020(25)}") // Valid(25)
//    val or10or20 = gt10 or gt20
//    println(s"Or: ${or10or20(5)}")  // Invalid(List(not gt10, not gt20))
//    println(s"Or: ${or10or20(15)}") // Valid(15)
//    println(s"Or: ${or10or20(25)}") // Valid(25)
  }

  test()
}
