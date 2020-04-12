package cats

object C206UseCase10ValidPredCheck extends App {

  import cats.data.Validated
  import cats.data.Validated.{Invalid, Valid}
  import cats.instances.list._
  import C206UseCase10ValidatedPred._

  sealed trait Check[E, A, B] {
    import Check._

    def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, B]
    def map[C](f: B => C): Check[E, A, C] = Map[E, A, B, C](this, f)
  }

  object Check {
    def apply[E, A](p: Predicate[E, A]): Check[E, A, A] = Pure(p)

    final case class Pure[E, A](p: Predicate[E, A]) extends Check[E, A, A] {
      def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, A] = p(in)
    }
    final case class Map[E, A, B, C](check: Check[E, A, B], f: B => C) extends Check[E, A, C] {
      def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, C] = check(in).map(f)
    }
  }

  def test_ch(): Unit = {
    val gt10: Predicate[List[String], Int] =
      Pure { x: Int => if (x>10) Valid(x) else Invalid(List("not gt10")) }

    val chGt10:   Check[List[String], Int, Int]    = Check(gt10)
    val chGt10m1: Check[List[String], Int, Int]    = chGt10.map { _ + 1}
    val chGt10m2: Check[List[String], Int, String] = chGt10.map { x => s"$x !" }
    val chGt10m3: Check[List[String], Int, String] = chGt10m2.map(s => s"_${s}_")

    println(chGt10m1(15))
    println(chGt10m1(5))
    println(chGt10m2(15))
    println(chGt10m2(5))
    println(chGt10m3(15))
    println(chGt10m3(5))
  }

  test_ch()
}
