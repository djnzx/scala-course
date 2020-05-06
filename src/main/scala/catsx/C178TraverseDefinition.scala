package catsx

import cats.Applicative

object C178TraverseDefinition extends App {
  trait Traverse[F[_]] {
    def traverse[G[_]: Applicative, A, B](data: F[A])(f: A => G[B]): G[F[B]]
    def sequence[G[_]: Applicative, B](data: F[G[B]]): G[F[B]]
  }
}
