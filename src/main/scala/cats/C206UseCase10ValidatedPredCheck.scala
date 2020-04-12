package cats

object C206UseCase10ValidatedPredCheck extends App {

  import cats.data.Validated
  import cats.data.Validated.{Invalid, Valid}
  import cats.instances.list._
  import cats.syntax.apply._
  import cats.syntax.semigroup._ // |+|
  import C206UseCase10ValidatedPred._

  sealed trait Check[E, A] {
    def apply(a: A)(implicit ev: Semigroup[E]): Validated[E, A]
//    def map[C](f: B => C): Check[E, A, C] = Map[E, A, B, C](this, f)
  }
  object Check {
    def apply[E, A](p: Predicate[E, A]): Check[E, A] = ChPure(p)
  }
//  final case class Map[E,A,B,C](check: Check[E, A, B], f: B => C) extends Check[E, A, C] {
//    def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, C] = check(in).map(f)
//  }
  final case class ChPure[E, A](p: Predicate[E, A]) extends Check[E, A] {
    def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, A] = p(in)
  }

  def test_ch(): Unit = {
    val gt10: Predicate[List[String], Int] =
      Pure { x: Int => if (x>10) Valid(x) else Invalid(List("not gt10")) }

    val chGt20: Check[List[String], Int] = Check(gt10)
    val r1: Validated[List[String], Int] = chGt20(5)
    println(r1)
  }

  test_ch()
}
