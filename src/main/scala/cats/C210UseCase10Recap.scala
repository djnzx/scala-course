package cats

import cats.data.NonEmptyList

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

  def longerThat(n: Int): Predicate[Errors, String] = Predicate.lift(
    error(s"must be longer that $n character"),
    (s: String) => s.length > n
  )

  def isAlphanumeric: Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain only alphanumeric chars"),
    s => s.forall(_.isLetterOrDigit)
  )
}
