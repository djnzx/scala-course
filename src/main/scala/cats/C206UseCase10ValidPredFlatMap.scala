package cats

object C206UseCase10ValidPredFlatMap extends App {

  import C206UseCase10ValidatedPred._
  import cats.data.Validated
  import cats.data.Validated.{Invalid, Valid}
  import cats.instances.list._

  sealed trait Check[E, A, B] {
    import Check._

    def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, B]
    // we map successful validation (right part)
    def map[C](f: B => C): Check[E, A, C] = Map[E, A, B, C](this, f)
    // we map current validation on another validation which can fail
    def flatMap[C](f: B => Check[E, A, C]): Check[E, A, C] = FlatMap[E, A, B, C](this, f)
    // handy chaining
    def andThen[C](next: Check[E, B, C]): Check[E, A, C] = AndThen[E, A, B, C](this, next)
  }

  object Check {
    def apply[E, A](p: Predicate[E, A]): Check[E, A, A] = Pure(p)

    final case class Pure[E, A](p: Predicate[E, A]) extends Check[E, A, A] {
      def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, A] = p(in)
    }
    final case class Map[E, A, B, C](check: Check[E, A, B], f: B => C) extends Check[E, A, C] {
      def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, C] = {
        val veb: Validated[E, B] = check(in)
        val vec: Validated[E, C] = veb.map(f)
        vec
      }
    }
    final case class FlatMap[E, A, B, C](check: Check[E, A, B], f: B => Check[E, A, C]) extends Check[E, A, C] {
      def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, C] = {
        val veb: Validated[E, B] = check(in)
        val eeb: Either[E, B] = veb.toEither
        def fn(b: B)(a: A): Validated[E, C] = f(b)(a)
        /**
          *  whether:
          * - keep E,         produce [E, Nothing]
          * - produce EE,     produce [EE, Nothing]
          * - convert B to C  produce [Nothing, C]
          */
        val vec = eeb.flatMap(b => fn(b)(in).toEither)
        Validated.fromEither(vec)
        /** can be done in one line:
          * because withEither(f) === Validated.fromEither(f(toEither))
          */
        veb.withEither(e => e.flatMap(b => f(b)(in).toEither))
      }
    }
    final case class AndThen[E, A, B, C](first: Check[E, A, B], second: Check[E, B, C]) extends Check[E, A, C] {
      override def apply(in: A)(implicit ev: Semigroup[E]): Validated[E, C] = {
        val veb: Validated[E, B] = first(in)
//        val eec: Either[E, C] = veb.toEither.flatMap(b => second(b).toEither)
//        Validated.fromEither(eec)
        // or
//        Validated.fromEither(veb.toEither.flatMap(b => second(b).toEither))
        // or
//        veb.withEither(eeb => eeb.flatMap(b => second(b).toEither))
        // or
        veb.withEither(_.flatMap(second(_).toEither))
      }
    }
  }

  def test_ch(): Unit = {
    val gt10: Predicate[List[String], Int] =
      Predicate { x: Int => if (x>10) Valid(x) else Invalid(List("not gt10")) }
    val gt20: Predicate[List[String], Int] =
      Predicate { x: Int => if (x>20) Valid(x) else Invalid(List("not gt20")) }

    val chGt10:    Check[List[String], Int, Int] = Check(gt10)
    val chGt20:    Check[List[String], Int, Int] = Check(gt20)
    val combined:  Check[List[String], Int, Int] = chGt10 flatMap { _ => chGt20 }
    val combined2: Check[List[String], Int, Int] = chGt10 andThen chGt20

    println(combined(5))   // failed on 1st
    println(combined(15))  // failed on 2nd
    println(combined2(25)) // passed both
  }

  test_ch()
}
