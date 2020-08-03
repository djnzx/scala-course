package fp_red.red12

import fp_red.red06.State

/**
  * if we have monad, we don't need to define applicative `map2`,
  * we can define `flatMap` and `unit`.
  * All monads are applicative functors.
  */
trait Monad[F[_]] extends Applicative[F] {
  def flatMap[A,B](ma: F[A])(f: A => F[B]): F[B] = join(map(ma)(f))

  /** remove the layer "F" */
  def join[A](mma: F[F[A]]): F[A] = flatMap(mma)(ma => ma)

  def compose[A,B,C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => flatMap(f(a))(g)

  override def apply[A,B](mf: F[A => B])(ma: F[A]): F[B] =
    flatMap(mf)(f => map(ma)(a => f(a)))

  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => unit(f(a)))

  override def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
    flatMap(fa)(a => map(fb)(b => f(a, b)))
}

object Monad {
  
  def eitherMonad[E]: Monad[({ type f[x] = Either[E, x] })#f] = 
    new Monad[({ type f[x] = Either[E, x]})#f] {
      override def unit[A](a: => A): Either[E, A] = Right(a)
      override def flatMap[A, B](ma: Either[E, A])(f: A => Either[E, B]): Either[E, B] = 
        ma.flatMap(f)
    }

  def stateMonad[S]: Monad[({ type f[x] = State[S, x] })#f] =
    new Monad[({type f[x] = State[S, x]})#f] {
      def unit[A](a: => A): State[S, A] = State(s => (a, s))
      override def flatMap[A,B](st: State[S, A])(f: A => State[S, B]): State[S, B] =
        st flatMap f
    }

  def composeM[G[_],H[_]](implicit G: Monad[G], H: Monad[H], T: Traverse[H]): Monad[({type f[x] = G[H[x]]})#f] =
    new Monad[({type f[x] = G[H[x]]})#f] {
      def unit[A](a: => A): G[H[A]] = G.unit(H.unit(a))
      
      override def flatMap[A,B](gha: G[H[A]])(f: A => G[H[B]]): G[H[B]] =
        G.flatMap(gha) { ha: H[A] => 
          G.map(T.traverse(ha)(f))(H.join)
        }
    }
  
}
