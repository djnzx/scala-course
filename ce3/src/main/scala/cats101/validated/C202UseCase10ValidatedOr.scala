package cats101.validated

import cats.Semigroup

object C202UseCase10ValidatedOr extends App {
  import cats.data.Validated
  import cats.data.Validated.{Invalid, Valid}
  import cats.instances.list._
  import cats.syntax.apply._
  import cats.syntax.semigroup._ // |+|

  type CheckFn[E, A] = A => Validated[E, A]

  sealed trait Check[E, A] {
    def and(that: Check[E, A]): Check[E, A] = And(this, that)
    def or(that: Check[E, A]): Check[E, A] = Or(this, that)

    def apply(a: A)(implicit ev: Semigroup[E]): Validated[E, A] = this match {
      case Pure(f) => f(a)
      case And(lf, rf) => (lf(a), rf(a)).mapN((_, _) => a)
      case Or(lf, rf) => (lf(a), rf(a)) match {
        case (Invalid(e1), Invalid(e2)) => Invalid(e1 |+| e2)
        case _ => Valid(a)
      }
    }
  }

  final case class Pure[E, A](f: CheckFn[E, A]) extends Check[E, A]
  final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
  final case class Or[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]

  def test(): Unit = {
    val gt10: Check[List[String], Int] =
      Pure { x: Int => if (x>10) Validated.valid(x) else Validated.invalid(List("not gt10")) }
    val gt20: Check[List[String], Int] =
      Pure { x: Int => if (x>20) Validated.valid(x) else Validated.invalid(List("not gt20")) }
    val noFail: Check[List[String], Int] =
      Pure { x: Int => Validated.valid(x) }
    val gt1020: Check[List[String], Int] =
      noFail and gt10 and gt20
    println(s"And: ${gt1020(5)}")  // Invalid(List(not gt10, not gt20))
    println(s"And: ${gt1020(15)}") // Invalid(List(not gt20))
    println(s"And: ${gt1020(25)}") // Valid(25)
    val or10or20 = gt10 or gt20
    println(s"Or: ${or10or20(5)}")  // Invalid(List(not gt10, not gt20))
    println(s"Or: ${or10or20(15)}") // Valid(15)
    println(s"Or: ${or10or20(25)}") // Valid(25)
  }

  test()

  // idea
  def map[E, A, B](that: Check[E, A])(f: A => B): Check[E, B] = ???
}
