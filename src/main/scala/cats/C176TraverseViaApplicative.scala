package cats

import cats.instances.list._
import cats.syntax.applicative._ // pure
import cats.syntax.apply._       // mapN

object C176TraverseViaApplicative extends App {

  // traverse via traverse
  def listTraverseT[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    Traverse[List].traverse(list)(func)

  // traverse via applicative
  def zero[F[_]:Applicative, B]: F[List[B]] = List.empty[B].pure[F]

  def listTraverseA1[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(zero[F, B])((acc, item) => {
      val fb: F[B] = func(item)
      val p: F[(List[B], B)] = Apply[F].product(acc, fb)      // tuple them
      val r: F[List[B]] = Apply[F].map(p)(t => t._1 :+ t._2)  // map to ...
      r
    })

  // traverse unfolded implementation
  def listTraverseA1a[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(zero[F, B])((acc, item) => {
      // extract instance
      val af: Apply[F] = Apply[F]
      val fb: F[B] = func(item)
      // method syntax
      def fa (a: List[B])(b: B): (List[B], B) = (a, b)
      // function syntax
      val fa2 = (a: List[B]) => (b: B) => (a, b)
      val ff: F[B => (List[B], B)] = af.map(acc)(fa)
      val p : F[(List[B], B)] = af.ap(ff)(fb)
      //
      val r: F[List[B]] = af.map(p)(t => t._1 :+ t._2)
      r
    })

  def listTraverseA3[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(zero[F, B])((acc, item) => {
      val fb: F[B] = func(item)
      val r: F[List[B]] = (acc, fb).mapN(_ :+ _)
      r
    })

  { //
    def listTraverseA7[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
      list.foldLeft(zero[F, B])((acc, item) => {
        val functor  = Functor[F]
        val semigroupal = Semigroupal[F]
        val fb: F[B] = func(item)
//        val r = functor.map(semigroupal.product(acc, fb)) { case (a0, a1) => (a0, a1) => func(a0, a1) }
        val r1: F[List[B]] = Semigroupal.map2(acc, fb)(_ :+ _)
        r1
      })
  }

  { // Semigroupal direct call
    def listTraverseA8[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
      list.foldLeft(zero[F, B])((acc, item) => {
        val fb: F[B] = func(item)
        val r: F[List[B]] = Semigroupal.map2(acc, fb)(_ :+ _)
        r
      })
  }

  { // mapN syntax for Semigroupal
    import cats.syntax.apply._       // mapN

    def listTraverseA9[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
      list.foldLeft(zero[F, B])((acc, item) => {
        (acc, func(item)).mapN(_ :+ _)
      })
  }

}
