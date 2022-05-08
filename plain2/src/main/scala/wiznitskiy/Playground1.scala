package wiznitskiy

object Playground1 extends App {

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  def inject[F[_]: Functor, A, B](fa: F[A], b: B): F[(A, B)] =
    implicitly[Functor[F]].map(fa)(a => (a, b))

}
