package fp_red.red11

import fp_red.red08.{Prop, SGen}

object FunctorLaws {

  def identityLaw[F[_], A](functor: Functor[F])(in: SGen[F[A]]): Prop =
    Prop.forAll(in) { fa: F[A] =>
      functor.map(fa)(identity) == fa
    }

  def compositionLaw[F[_], A, B, C](functor: Functor[F])(f: A => B, g: B => C)(in: SGen[F[A]]): Prop =
    Prop.forAll(in) { fa: F[A] =>
      val aToC: A => C = f andThen g
      val fc1: F[C] = functor.map(fa)(aToC)

      val fb: F[B] = functor.map(fa)(f)
      val fc2: F[C] = functor.map(fb)(g)

      fc1 == fc2
    }

  def leftAssocLaw[F[_], A, B](functor: Functor[F])(f: A => B)(in: SGen[F[A]]): Prop =
    Prop.forAll(in) { fa: F[A] =>
      functor.map(fa)(f andThen identity[B]) == functor.map(fa)(f)
    }

  def rightAssocLaw[F[_], A, B](functor: Functor[F])(f: A => B)(in: SGen[F[A]]): Prop =
    Prop.forAll(in) { fa: F[A] =>
      functor.map(fa)(identity[A] _ andThen f) == functor.map(fa)(f)
    }

}
