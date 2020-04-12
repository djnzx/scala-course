package cats

import cats.data.{NonEmptyList, Validated}

object C210UseCase10Recap extends App {
  import C206UseCase10ValidPredFlatMap.Check
  import C206UseCase10ValidatedPred._

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
  def error(line: String): NonEmptyList[String] = NonEmptyList(line, Nil)

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
    error(s"Must contain char `$c` only once"),
    s => s.count(_ == c) == 1
  )

  val leftToAt = (s: String) => s.substring(0, s.indexOf('@'))
  val rightFromAt = (s: String) => s.substring(s.indexOf('@')+1)

  case class User(name: String, email: String)

  def validate(name: String, email: String): Validated[Errors, User] = {

    import cats.syntax.apply._

    val nameValidator = Check(longerThan(3) and isAlphanumeric)
    val emailValidator =
      Check(containsOnce('@')) andThen Check(
          Pure { s: String => longerThan(3)(leftToAt(s)) } and
          Pure { s: String => longerThan(2)(rightFromAt(s)) }
      ) andThen Check(contains('.'))

    ( nameValidator(name),
      emailValidator(email)
      ).mapN(User.apply)
  }
  val r: Validated[Errors, User] = validate("alex","alexr@gmail.com")
  println(r)
}
