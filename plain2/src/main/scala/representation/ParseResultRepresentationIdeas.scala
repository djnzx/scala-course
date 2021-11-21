package representation

object ParseResultRepresentationIdeas {

  sealed trait ParseResult[+E, +A]

  object ParseResult {

    /** everything is OK */
    final case class Success[A](a: A) extends ParseResult[Nothing, A]

    /** failed, no result */
    final case class Fatal[E](e: E) extends ParseResult[E, Nothing]

    /** processed OK, but with some warnings */
    final case class Warning[A, E](a: A, e: E) extends ParseResult[E, A]
  }

}
