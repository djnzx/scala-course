package catsx

import cats.data.{NonEmptyList, Validated}

object C210UseCase10Recap extends App {
  import C206UseCase10ValidPredFlatMap.Check
  import C206UseCase10ValidatedPred._

  import cats.syntax.apply._

  /**
    * A username
    * - must contain at least four characters
    * - and consist entirely of alphanumeric characters
    *
    * An email address
    * - must contain an @ sign.
    * Split the string at the @.
    * - The string to the left must not be empty.
    * - The string to the right must be at least three characters long and
    * - contain a dot
    */

  type Errors = NonEmptyList[String]
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

  val nameValidator = longerThan(3) and isAlphanumeric

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

  val emailValidator =
    Check(containsOnce('@')) map splitToTuple andThen Check(tupleValidator) map tupleCombine

  case class User(name: String, email: String)

  def validate(name: String, email: String): Validated[Errors, User] =
    ( nameValidator(name),
      emailValidator(email)
      ).mapN(User.apply)

  println(validate("alex","alexr@gmail.com"))
  println(validate("ale","alexr@gmail.com")) // Invalid(NonEmptyList(must be longer that 3 character))
  println(validate("alex","@gmail.com"))     // Invalid(NonEmptyList(must be longer that 0 character))
  println(validate("alex","@gma"))           // Invalid(NonEmptyList(must be longer that 0 character, must be longer that 3 character, Must contain at least one `.` char))
}
