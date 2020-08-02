package fp_red.red11

import fp_red.red08.{Prop, SGen}

object MonadLaws {

  /**
    * compose(f, unit) == f  // f: A => B, unit B => B
    * or:
    * flatMap(x)(unit) == x
    */
  def leftIdentityLaw[F[_], A](monad: Monad[F])(in: SGen[F[A]]): Prop =
    Prop.forAll(in) { fa: F[A] =>
      monad.flatMap(fa)(monad.unit(_)) == fa
    }

  /**
    * compose(unit, f) == f  // unit A => A, f: A => B
    * or:
    * flatMap(unit(y))(f) == f(y)
    */
  def rightIdentityLaw[F[_], A, B](monad: Monad[F])(in: SGen[F[A]])(fab: A => F[B]): Prop =
    Prop.forAll(in) { fa: F[A] =>
      val fb: F[B] = monad.flatMap(fa)(fab)
      
      val fa2: F[A] = monad.flatMap(fa)(a => monad.unit(a))
      val fb2: F[B] = monad.flatMap(fa2)(fab)
      
      fb == fb2
    }

  /**
    * x.flatMap(f).flatMap(g) == x.flatMap(a => f(a).flatMap(g))
    * or:
    * compose(compose(f, g), h) == compose(f, compose(g, h))
    */
  def associativityLaw[F[_], A, B, C](monad: Monad[F])(in: SGen[F[A]])(fab: A => F[B], fbc: B => F[C]): Prop =
    Prop.forAll(in) { fa: F[A] =>
      val fb:  F[B] = monad.flatMap(fa)(fab)
      val fc1: F[C] = monad.flatMap(fb)(fbc)
      
      val fc2: F[C] = monad.flatMap(fa)(a => monad.flatMap(fab(a))(fbc))
      
      fc1 == fc2
    }
  
}