package cats101

import cats.Semigroup

object C206UseCase10ValidatedPred extends App {
  import cats.data.Validated
  import cats.data.Validated.{Invalid, Valid}
  import cats.instances.list._
  import cats.syntax.apply._     // mapN
  import cats.syntax.semigroup._ // |+|
  import cats.syntax.validated._ // .valid, .invalid

  type CheckFn[E, A] = A => Validated[E, A]
  sealed trait Predicate[E, A] {
    import Predicate._

    def apply(a: A)(implicit ev: Semigroup[E]): Validated[E, A] = this match {
      case Pure(f) => f(a)
      case And(lf, rf) => (lf(a), rf(a)).mapN((_, _) => a)
      case Or(lf, rf) => (lf(a), rf(a)) match {
        case (Invalid(e1), Invalid(e2)) => Invalid(e1 |+| e2)
        case _ => Valid(a)
      }
    }

    def and(that: Predicate[E, A]): Predicate[E, A] = And(this, that)
    def or(that: Predicate[E, A]): Predicate[E, A] = Or(this, that)
  }

  object Predicate {
    def apply[E, A](f: CheckFn[E, A]): Predicate[E, A] = Pure(f)
    final case class Pure[E, A](f: CheckFn[E, A]) extends Predicate[E, A]
    final case class And[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Or[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    def lift[E, A](err: E, p: A => Boolean): Predicate[E, A] =
      Pure { a => if (p(a)) a.valid else err.invalid }
  }

  def test(): Unit = {
    val gt10: Predicate[List[String], Int] =
      Predicate { x: Int => if (x>10) Valid(x) else Invalid(List("not gt10")) }
    val gt20: Predicate[List[String], Int] =
      Predicate { x: Int => if (x>20) Valid(x) else Invalid(List("not gt20")) }
    val noFail: Predicate[List[String], Int] =
      Predicate { x: Int => Valid(x) }
    val gt1020: Predicate[List[String], Int] =
      noFail and gt10 and gt20
    val or10or20: Predicate[List[String], Int] =
      gt10 or gt20

    val fn = (a: Int) => if (a>10) Validated.valid(a) else Validated.invalid(List("Wrong"))
    // object Predicate.apply
    val pfn: Predicate[List[String], Int] = Predicate(fn)
    // trait Predicate.apply
    pfn(1)

    println(s"And: ${gt1020(5)}")  // Invalid(List(not gt10, not gt20))
    println(s"And: ${gt1020(15)}") // Invalid(List(not gt20))
    println(s"And: ${gt1020(25)}") // Valid(25)
    println(s"Or: ${or10or20(5)}")  // Invalid(List(not gt10, not gt20))
    println(s"Or: ${or10or20(15)}") // Valid(15)
    println(s"Or: ${or10or20(25)}") // Valid(25)
  }
  test()
}
