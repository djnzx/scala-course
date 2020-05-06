package catsx

object C201UseCase10ValidADT extends App {
  import cats.Semigroup
  import cats.instances.list._
  import cats.syntax.either._
  import cats.syntax.semigroup._

  type CheckFn[E, A] = A => Either[E, A]

  sealed trait Check[E, A] {
    def and(that: Check[E, A]): Check[E, A] = And(this, that)

    def apply(a: A)(implicit ev: Semigroup[E]): Either[E, A] = this match {
      case Pure(f) => f(a)
      case And(lf, rf) => (lf(a), rf(a)) match {
        case (Left(e1),  Left(e2)) => (e1 |+| e2).asLeft[A]
        case (Left(e1),  Right(_)) => e1.asLeft[A]
        case (Right(_),  Left(e2)) => e2.asLeft[A]
        case (Right(v1), Right(_)) => v1.asRight[E]
      }
    }
  }

  // holds only one function
  final case class Pure[E, A](f: CheckFn[E, A]) extends Check[E, A]
  // hold both left and right
  final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]

  def test_and(): Unit = {
    val gt10: Check[List[String], Int] =
      Pure { x: Int => if (x>10) x.asRight else List("not gt10").asLeft[Int] }
    val gt20: Check[List[String], Int] =
      Pure { x: Int => if (x>20) x.asRight else List("not gt20").asLeft[Int] }
    val noFail: Check[List[String], Int] =
      Pure { x: Int => x.asRight[List[String]] }
    val gt1020: Check[List[String], Int] =
      noFail and gt10 and gt20
    println(gt1020(5))
    println(gt1020(15))
    println(gt1020(25))
  }

  test_and()


}
