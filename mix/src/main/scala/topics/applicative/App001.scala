package topics.applicative

import cats.{Apply, FlatMap, Functor}
//import cats.syntax.functor._
//import cats.syntax.flatMap._

object App001 extends App {

  /**
    * but actually we need to have just implementations for this interface
    */
  def map[F[_]: Functor, A, B](fa: F[A])(f: A => B)(implicit inst: Functor[F]): F[B] =
    inst.map(fa)(f)

  /**
    * map2 implementation via flatMap
    * based on flatMap (actually Monad)
    */
  def map2m[F[_], A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C)(implicit inst: FlatMap[F]): F[C] =
    inst.flatMap(fa)(a => inst.map(fb)(b => f(a, b)))
//    fa.flatMap(a => fb.map(b => f(a, b)))

  /**
    * ap implementation
    * based on flatMap (actually Monad)
    */
  def ap[F[_]: FlatMap, A, B](fa: F[A])(ff: F[A => B])(implicit inst: FlatMap[F]): F[B] =
    inst.flatMap(fa)(a => inst.map(ff)(f => f(a)))
//    fa.flatMap(a => ff.map(f => f(a)))

  /**
    * but we want to implement
    * (F[A], F[B], f:(A, B) => C): F[C]
    * without Monadic dependency
    * supposing, that B doesn't depend on A
    *
    * Apply has only:
    * - def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
    * - def map[F[_]: Functor, A, B](fa: F[A])(f: A => B): F[B]
    */
  def map2a[F[_], A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C)(implicit af: Apply[F]): F[C] = {
    /**
      * af has only `.map` for function with one parameter
      * let's represent our f: (A, B) => C
      * in different way: fabc: A => B => C
      * and after that we can pass our params during different iterations
      */
    val fabc: A => B => C = (a: A) => (b: B) => f(a, b)
    /**
      * right now, we can do the first step:
      * map F[A] over function A => B => C
      * and get F[B => C]
      */
    val fbc: F[B => C] = af.map(fa)(fabc)
    /**
      * let's use power of Applicative
      * and fuse F[B] and F[B => C]
      */
    val fc: F[C] = af.ap(fbc)(fb)

    fc
  }
  /**
    * but that's kind of primitive operation which can't be expressed
    * via any more simplified function, so
    * we need to have particular instances
    */

  /**
    * lets implement Apply for list
    */
  val al = new Apply[List] {
    // we use list embedded implementation
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)

    /**
      * if our structure doesn't have flatMap
      * implementation is way more complicated
      */
    override def ap[A, B](ff: List[A => B])(fa: List[A]): List[B] =
      fa.flatMap(a => ff.map(f => f(a)))
  }


}
