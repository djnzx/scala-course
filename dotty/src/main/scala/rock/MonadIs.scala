package rock

object MonadIs {

  /** monoid in a category A[_] */
  trait MonoidInCategoryK2[A[_], ~>[_[_], _[_]], E[_], P[_]] {
    def unit: E ~> A
    def combine: P ~> A
  }

  /** monoid in a category A */
  trait MonoidInCategory[A, ~>[_, _], E, P] {
    def unit: E ~> A    // ~> [E, A]
    def combine: P ~> A // ~> [P, A]
  }

  /** define _ ~> _ as _ => _ */
  trait GeneralMonoid[A, E, P] extends MonoidInCategory[A, Function1, E, P] {
//    def unit: E => A
//    def combine: P => A
  }

  /** define E as a Unit, P as a (A, A) */
  trait FunctionalMonoid[A] extends GeneralMonoid [A, Unit, (A, A)] {
//    def unit: Unit => A
//    def combine: ((A, A)) => A
  }

  /** monoid  as we accustomed */
  trait Monoid[A] extends FunctionalMonoid[A] {
    def empty: A
    def combine(a1: A, a2: A): A
    // combine(a, combine(b, c)) == combine(combine(a, b), c)
    // combine(a, empty) == a
    // combine(empty, a) == a

    // hidden
    def unit = _ => empty
    // inherited implemented via our functionality
    def combine: ((A, A)) => A = (pa: (A, A)) => combine(pa._1, pa._2)
  }

  object IntAddition extends Monoid[Int] {
    override def empty: Int = 0
    override def combine(a1: Int, a2: Int): Int = a1 + a2
  }

  /** endofunctors in the same cathegory F
    * F[A] => F[B]
    */
  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  given functorList: Functor[List] with {
    def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  /** functor transformation */
  trait MyFunction1[-A, +B] {
    def apply(a: A): B
  }

  /** natural transformation */
  trait FunctorNaturalTransformation[-F[_], +G[_]] { // ~>[F, G] === F ~> G
    def apply[A](fa: F[A]): G[A]
  }

  /**
    * and also
    * .to*******
    *
    * all are the natural transformations
    */
  object ListToOptionTrans extends FunctorNaturalTransformation[List, Option] {
    override def apply[A](fa: List[A]): Option[A] = fa.headOption
  }

  /** id functor */
  type Id[A] = A

  given idFunctor: Functor[Id] with {
    override def map[A, B](fa: A)(f: A => B): B = f(fa)
  }

  /** function composition */
  def funcComposition[A, B, C](f: A => B, g: B => C, x: A) = g(f(x))

  type HKTComposition[F[_], G[_], A] = G[F[A]]
  type SameTypeComposition[F[_], A] = F[F[A]]

  // doesnt work
//  trait MonoidInCategoryOfFunctors[F[_]: Functor] extends MonoidInCategoryK2[F, FunctorNaturalTransformation, Id, F[F[_]]]
  // type lambda syntax
  trait MonoidInCategoryOfFunctors[F[_]: Functor] extends MonoidInCategoryK2[F, FunctorNaturalTransformation, Id, [A] =>> F[F[A]]] {
    type FunctorProduct[A] = F[F[A]]

    /** lifting value to a context */
//    override def unit: FunctorNaturalTransformation[Id, F]
    /** flatMap */
//    override def combine: FunctorNaturalTransformation[FunctorProduct, F]
    def pure[A](a: A): F[A] = unit.apply(a) // constructor, lifter
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = {
      val functor: Functor[F] = summon[Functor[F]] // implicitly
      val ffb: F[F[B]] = functor.map(fa)(f)
      val fb: F[B] = combine.apply(ffb)
      fb
    }
  }

  trait Monad[F[F]] extends Functor[F] with MonoidInCategoryK2[F, FunctorNaturalTransformation, Id, [A] =>> F[F[A]]] {
    // public
    def pure[A](a: A): F[A]
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

    def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(a => pure(f(a)))

    def flatten[A](ffa: F[F[A]]): F[A] = flatMap(ffa)(x => x)

    type FunctorProduct[A] = F[F[A]]

    def unit: FunctorNaturalTransformation[Id, F] = new FunctorNaturalTransformation[Id, F] {
      override def apply[A](fa: Id[A]): F[A] = pure(fa)
    }

    def combine: FunctorNaturalTransformation[FunctorProduct, F] = new FunctorNaturalTransformation[FunctorProduct, F] {
      override def apply[A](fa: FunctorProduct[A]): F[A] = flatten(fa)
    }
  }


  object ListSpecialMonoid extends MonoidInCategoryOfFunctors[List] {
    override def unit: FunctorNaturalTransformation[Id, List] = new FunctorNaturalTransformation[Id, List] {
      override def apply[A](fa: Id[A]): List[A] = List(fa)
    }

    /**
      * FunctorProduct[A] == F[F[A]] == List[List]]
      * F = List
      */
    override def combine: FunctorNaturalTransformation[FunctorProduct, List] = new FunctorNaturalTransformation[FunctorProduct, List] {
      override def apply[A](fa: List[List[A]]): List[A] = fa.flatMap(x => x)
    }
  }

  def main(args: Array[String]): Unit = {
    println("too abstract...")

    val combinerFn: ((Int, Int)) => Int = IntAddition.combine
    val x: Int = IntAddition.combine(1, 2)

    println(IntAddition.empty)
    println(x)

    println(ListSpecialMonoid.combine(List(
      List(1, 2, 3),
      List(4, 5, 6),
      List(7, 8, 9),
    )))

  }

}
