package catsx.kleisli_valid

import cats.Semigroup
import cats.data.Validated.{Invalid, Valid}
import cats.data.{Kleisli, NonEmptyList, Validated}
import cats.syntax.apply._
import cats.syntax.semigroup._
import cats.syntax.validated._

object KleisliValidated extends App {
  // that type will handle our errors
  type Errors = NonEmptyList[String]

  sealed trait Predicate[E, A] {
    import Predicate._

    def run(implicit ev: Semigroup[E]): A => Either[E, A] = (a: A) => this(a).toEither

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
    def apply[E, A](f: A => Validated[E, A]): Predicate[E, A] = Pure(f)
    final case class Pure[E, A](f: A => Validated[E, A]) extends Predicate[E, A]
    final case class And[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Or[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    def lift[E, A](err: E, p: A => Boolean): Predicate[E, A] =
      Pure { a: A => if (p(a)) a.valid else err.invalid }
  }

  /**
    * so, we will use A => Result[B]
    * and it can be represented
    * Kleisli[Result, A, B]
    */
  type VResult[A] = Either[Errors, A]
  type Check[A, B] = Kleisli[VResult, A, B]
  /**
    * create a Check from a function
    */
  def funcToCheck[A, B](f: A => VResult[B]): Check[A, B] = Kleisli { f }
  /**
    * create a Check from a predicate
    */
  def predToCheck[A](p: Predicate[Errors, A]): Check[A, A] = Kleisli { p.run }

  import cats.instances.either._
  import cats.syntax.apply._

  /**
    * usecase
    */
  def error(line: String) = NonEmptyList(line, Nil)

  def longerThan(n: Int): Predicate[Errors, String] = Predicate.lift(
    error(s"must be longer that $n character"),
    s => s.length > n
  )

  def isAlphanumeric: Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain only alphanumeric chars"),
    s => s.forall(_.isLetterOrDigit)
  )

  def contains(c: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain at least one `$c` char"),
    s => s.contains(c)
  )

  def containsOnce(c: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain single `$c` char"),
    s => s.count(_ == c) == 1
  )

  val splitToTuple = (s: String) => {
    val at = s.indexOf('@')
    (s.substring(0, at), s.substring(at + 1))
  }
  val tupleValidator: Predicate[Errors, (String, String)] = Predicate { case (address, domain) =>
    (longerThan(0)(address),
      (longerThan(3) and contains('.'))(domain)
      ).mapN((_, _))
  }
  val tupleCombine: ((String, String)) => String = { case (addr, dom) => s"$addr@$dom" }

  case class User(name: String, email: String)

  val emailValidator: Check[String, String] =
    predToCheck(containsOnce('@')) map splitToTuple andThen predToCheck(tupleValidator) map tupleCombine

  val nameValidator: Check[String, String] = predToCheck(longerThan(3) and isAlphanumeric)

  def validate(name: String, email: String): Either[Errors, User] =
    ( nameValidator(name),
      emailValidator(email)
      ).mapN(User.apply)

  /**
    * Kleisli relies on Monad.
    * so we need to use Either, it is a monad
    * because Validated isn't
    */

  println(validate("alex","alexr@gmail.com"))
  println(validate("ale","alexr@gmail.com")) // Left(NonEmptyList(must be longer that 3 character))
  println(validate("alex","@gmail.com"))     // Left(NonEmptyList(must be longer that 0 character))
  println(validate("alex","@gma"))           // Left(NonEmptyList(must be longer that 0 character, must be longer that 3 character, Must contain at least one `.` char))

}
