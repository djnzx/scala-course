package creative.parsers

/** Indicates the result of a parse. */
sealed trait Result[+A] {
  def map[B](f: A => B): Result[B] =
    this match {
      case Success(result, input, offset) => Success(f(result), input, offset)
      case f: Failure                     => f
    }

  def isSuccess: Boolean =
    this match {
      case _: Success[_] => true
      case _: Failure    => false
    }

  def isFailure: Boolean =
    this match {
      case _: Success[_] => false
      case _: Failure    => true
    }
}
object Result {
  def success[A](result: A, input: String, offset: Int): Result[A] =
    Success(result, input, offset)

  def failure[A](reason: String, input: String, start: Int): Result[A] =
    Failure(reason, input, start)
}

/** The parse succeeded.
  *
  *   - result is the parsed value
  *   - input is the input that was parsed
  *   - offset is the index of where any remaining input starts.
  */
final case class Success[A](result: A, input: String, offset: Int)
    extends Result[A]

/** The parse failed.
  *
  *   - reason is a description of why the parser failed
  *   - input is the input that the parser attempted to parse
  *   - start is the index into input of where the parser started from
  */
final case class Failure(reason: String, input: String, start: Int)
    extends Result[Nothing]
