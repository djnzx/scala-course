package cats101.validated

object C202UseCase10Validated extends App {
  import cats.Semigroup
  import cats.data.Validated
  import cats.instances.list._
  import cats.syntax.apply._     // mapN

  type CheckFn[E, A] = A => Validated[E, A]

  sealed trait Check[E, A] {
    def and(that: Check[E, A]): Check[E, A] = And(this, that)

    def apply(a: A)(implicit ev: Semigroup[E]): Validated[E, A] = this match {
      case Pure(f) => f(a)
      case And(lf, rf) => (lf(a), rf(a)).mapN((_, _) => a)
    }
  }

  // holds only one function
  final case class Pure[E, A](f: CheckFn[E, A]) extends Check[E, A]
  // hold both left and right
  final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]

  def test_and(): Unit = {
    val gt10: Check[List[String], Int] =
      Pure { x: Int => if (x>10) Validated.valid(x) else Validated.invalid(List("not gt10")) }
    val gt20: Check[List[String], Int] =
      Pure { x: Int => if (x>20) Validated.valid(x) else Validated.invalid(List("not gt20")) }
    val noFail: Check[List[String], Int] =
      Pure { x: Int => Validated.valid(x) }
    val gt1020: Check[List[String], Int] =
      noFail and gt10 and gt20
    println(gt1020(5))
    println(gt1020(15))
    println(gt1020(25))
  }

  test_and()


}
