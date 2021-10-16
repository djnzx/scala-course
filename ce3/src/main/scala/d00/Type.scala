package d00

import cats.Applicative
import cats.Monad
import cats.~>

object Type {

  trait Parallel[S[_]] {
    type P[_]
    def monad: Monad[S]
    def applicative: Applicative[P]
    def sequential: P ~> S
    def parallel: S ~> P
  }

}
