package monadismonoid

object CategoryScala {

  trait ~>[_, _] {
    def id[A](a: A): A
  }

  trait Compose[F[_, _]] {
    def compose[A, B, C](f: F[B, C], g: F[A, B]): F[A, C]
    def andThen[A, B, C](f: F[A, B], g: F[B, C]): F[A, C] = compose(g, f)
  }

  trait Category[F[_, _]] extends Compose[F] { self =>
    def id[A]: F[A, A]
  }

  trait FunctionAB[-A, +B] extends Category[~>]

}
