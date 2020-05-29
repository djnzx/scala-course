package catsx

import cats.Monad

object C099MonadErrorIdea extends App {

  /**
    * MonadError is typed Monad with F[E]
    * F - monad type
    * E - error type
    */
  trait MonadError0[F[_], E] extends Monad[F] {

    // Lift an error into the `F` context:
    def raiseError[A](e: E): F[A]

    // Handle an error, potentially recovering from it:
    def handleError[A](fa: F[A])(f: E => A): F[A]

    // Test an instance of `F`,
    // failing if the predicate is not satisfied:
    def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
  }
}
