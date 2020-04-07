package cats

object C176TraverseViaApplicative extends App {

  /**
    * direct listTraverse via Traverse[List]traverse
    */
  {
    import cats.instances.list._

    def listTraverseT[F[_]: Applicative, A, B](list: List[A])(f: A => F[B]): F[List[B]] =
      Traverse[List].traverse(list)(f)
  }

  /**
    * Traverse via applicative
    * EMPTY element we need to fold to
    */
  import cats.syntax.applicative._ // .pure syntax to lift value
  def EMPTY[F[_]:Applicative, B]: F[List[B]] = List.empty[B].pure[F]

  /**
    * Traverse via applicative
    * mapN syntax for Semigroupal
    */
  {
    import cats.syntax.apply._       // mapN

    def listTraverseA1[F[_]: Applicative, A, B](list: List[A])(f: A => F[B]): F[List[B]] =
      list.foldLeft(EMPTY[F, B])((acc, item) => {
        // apply function A => F[B]
        val fb: F[B] = f(item)
        // mapN because F is a functor
        val r: F[List[B]] = (acc, fb).mapN(_ :+ _)
        r
      })
  }

  /**
    * Traverse via applicative
    * Semigroupal.map2 direct call
    */
  def listTraverseA2[F[_]: Applicative, A, B](list: List[A])(f: A => F[B]): F[List[B]] =
    list.foldLeft(EMPTY[F, B])((acc, item) => {
      // apply function A => F[B]
      val fb: F[B] = f(item)
      // Semigroupal.map2(...)(...)(implicit semigroupal: Semigroupal[F], functor: Functor[F])
      val r: F[List[B]] = Semigroupal.map2(acc, fb) { combinerFn1 }
      r
    })

  /**
    * Traverse via applicative
    * Semigroupal.map2 unfolded
    * tupled tupled: F[(List[B], B)] made by semigroupal.product
    */
  def listTraverseA3[F[_]: Applicative, A, B](list: List[A])(f: A => F[B]): F[List[B]] =
    list.foldLeft(EMPTY[F, B])((acc, item) => {
      // extract instances
      val functor: Functor[F] = Functor[F]
      val semigroupal = Semigroupal.apply[F]
      // apply function A => F[B]
      val fb: F[B] = f(item)
      // make product: def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
      val tupled: F[(List[B], B)] = semigroupal.product(acc, fb)
      // map syntax #1. look for syntax #2 in the listTraverseA4
      val r1 = functor.map(tupled) { t  => t._1 :+ t._2 }
      r1
    })

  /**
    * Traverse via applicative
    * without semigroupal.product
    * based only on Applicative and Functor
    * tupled tupled: F[(List[B], B)] made by Apply[F].ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
    * actually we describe dependent calculations as partially applied
    */
  def combinerFn1[B](lb: List[B], b: B): List[B] = lb :+ b
  def combinerFn2[B](t: (List[B], B)): List[B] = t._1 :+ t._2
  def combinerFn3[B](t: (List[B], B)): List[B] = t match { case (lb, b)  => lb :+ b }

  def listTraverseA4[F[_]: Applicative, A, B](list: List[A])(f: A => F[B])(
    implicit functor: Functor[F], af: Apply[F]
  ): F[List[B]] =
    list.foldLeft(EMPTY[F, B])((acc: F[List[B]], item) => {
      // apply function A => F[B]
      val fb: F[B] = f(item)
      /**
        * the task is to join `F[List[B]] (acc)` and F[B] (fb) without flatMap
        */
      val product_curry: List[B] => B => (List[B], B) =
        lb => b => (lb, b)

      val partially_applied: F[B => (List[B], B)] = functor.map(acc)     { product_curry }
      //                                                     F[List[B]]  lb => b => (lb, b)
      val tupled : F[(List[B], B)] = af.ap(partially_applied) { fb }
      //                                 F[B => (List[B], B)]  F[B]

      // map syntax #2. look for syntax #1 in the listTraverseA3
      val r2: F[List[B]] = functor.map(tupled) { combinerFn3 }
      r2
    })

  def listTraverseA5[F[_]: Applicative, A, B](list: List[A])(f: A => F[B])(implicit af: Apply[F]): F[List[B]] =
    list.foldLeft(EMPTY[F, B])((acc, item) =>
      af.map(
        af.ap(
          af.map(acc) { lb => (b: B) => (lb, b) }
        ) { f(item) }
      ) { combinerFn3 }
    )

}
