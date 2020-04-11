package cats

object C200UseCase10Valid extends App {
  /**
    * type to represent check
    * function which
    * takes anything of type A and:
    * returns Either[String, A]
    *
    * it takes the VALUE and lift it to CONTEXT
    */
  type Check1[A] = Either[String, A]
  type Check2[A] = Either[List[String], A]
  /**
    * combination:
    * - String * String => String
    * - List[String] * List[String] => List[String]
    */
  type Check3[E, A] = Either[E, A]
  type CheckFn[E, A] = A => Either[E, A]
  /**
    * collecting errors in the List[String]
    * we don't need a Monoid because we don't need an empty (identity) element
    * keep constraints as small as possible
    */
  import cats.Semigroup
  import cats.instances.list._   // Semigroup
  import cats.syntax.semigroup._ // |+|
  // via .combine()
  val semi = Semigroup[List[String]]
  val combined1: List[String] = semi.combine(List("E1"), List("E2"))
  // via |+| syntax
  val combined2: List[String] = List("E3") |+| List("E4")
  /**
    * if we want to have extra combiners
    * we define a trait
    */
  trait Check[E, A] {
    def apply(a: A): Either[E, A]
    // we will not short circuit if one fails, we need to collect everything
    def and(that: CheckFn[E, A])(implicit semi: Semigroup[E]): Check[E, A] = ???
    //...
  }
  import cats.syntax.either._

  final case class CheckF[E: Semigroup, A](f: CheckFn[E,A]) {
    def apply(a: A): Either[E, A] = f(a)
    def and(that: CheckF[E, A]): CheckF[E, A] = CheckF { a: A =>
      (f(a), that(a)) match {
        case (Left(e1), Left(e2))  => (e1 |+| e2).asLeft[A]
        case (Left(e1), Right(_))  => e1.asLeft[A]
        case (Right(_), Left(e2))  => e2.asLeft[A]
        case (Right(v1), Right(_)) => v1.asRight[E]
      }
    }
  }

  def test_and(): Unit = {
    /**
      * we need to use asRight[List[String] ]
      * to avoid error no implicit for Semigroup[Nothing]
      */
    val gt10 = CheckF { x: Int => if (x>10) x.asRight else List("not gt10").asLeft[Int] }
    val gt20 = CheckF { x: Int => if (x>20) x.asRight else List("not gt20").asLeft[Int] }
    val noFail = CheckF { x: Int => x.asRight[List[String]] }
    val gt1020 = noFail and gt10 and gt20
    println(gt1020(5))
    println(gt1020(15))
    println(gt1020(25))
  }



}
