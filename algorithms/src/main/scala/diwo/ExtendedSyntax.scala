package diwo

object ExtendedSyntax {
  
  /** syntax to throw an exception */
  implicit class ExSyntax(s: String) {
    def unary_! = throw new IllegalArgumentException(s)
  }
  
  /** missed Either combinators */
  implicit class RichEither[L, R](e: Either[L, R]) {
    def mapLeft[L2](f: L => L2) = e match {
      case Right(r) => Right(r)
      case Left(l)  => Left(f(l))
    }
    def or[R2 >: R](e2: Either[L, R2]) = e match {
      case Right(r) => Right(r)
      case _        => e2
    }
    def getOrDie = e.fold(!_.toString, identity)
  }

}
