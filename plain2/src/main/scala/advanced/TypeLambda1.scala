package advanced

/** TypeLambda is only about syntax:
  *
  * https://underscore.io/blog/posts/2016/12/05/type-lambdas.html
  *
  * https://github.com/typelevel/kind-projector
  *
  * https://thenewcircle.com/s/post/665/a_more_readable_type_lambda_trick
  */
object TypeLambda1 extends App {

  object intro {

    /** we can create type alias */
    type L = List[Option[(Int, Double)]]

    /** we can create type alias with type parameters */
    type OM[A] = Option[Map[Int, A]]

    /** we can use them */
    val m1: Option[Map[Int, String]] = Some(Map(1 -> "abc", 2 -> "xyz"))
    val m2: OM[String] = Some(Map(1 -> "abc", 2 -> "xyz"))
  }

  /** we have trait constructor with one hole */
  trait Functor[F[_]] {
    def as[A](a: A): F[A] = ???
  }

  /** this works */
  type F1 = Functor[Option]

  /** this also works */
  type F2 = Functor[List]

  /** this doesnt, Map takes two type parameters [K, V] */
//   type F3 = Functor[Map]
  /** we make the alias */
  type IntKeyMap[A] = Map[Int, A]

  /** this works but requires one extra line of code */
  type F3 = Functor[IntKeyMap]

  /** it would be good to write */
//  type F5 = Functor[Map[Int, _]]

  /** this syntax works, on a value level */
  val cube: Double => Double = Math.pow(_: Double, 3)

  /** when something expects F[_] (one type parameter), for example: */
  def wrap[F[_], A](data: F[A]): Vector[F[A]] = Vector(data)

  /** we can do */
  val d1: Option[Boolean] = Some(true)
  val d2: List[Double] = List(1.5)
  val d3: Map[Int, String] = Map(1 -> "a")
  val d4: Map[Int, Double] = Map(1 -> 1d)

  val w1: Vector[Option[Boolean]] = wrap(d1)
  val w2: Vector[List[Double]] = wrap(d2)

  /** what ??? */
  val w3: Vector[Map[Int, String]] = wrap(d3)
  val w4: Vector[Map[Int, Double]] = wrap(d4)

  object solution1extraLineLeakingType {
    // declare an alias
    type MI[A] = Map[Int, A]
    // use it
    type F5 = Functor[MI]
    // the only drawback is - MI[A] leaks outside
  }

  object solution2typeLambda {
    type F5 = Functor[({ type f[A] = Map[Int, A] })#f]
    val f5: Functor[({ type f[A] = Map[Int, A] })#f] =
      new Functor[({ type f[x] = Map[Int, x] })#f] {}
  }

  object solution3kindProjector {
    type F5 = Functor[Map[Int, *]]
    val f5: Functor[Map[Int, *]] = new Functor[Map[Int, *]] {}
  }

  object problem2 {

    /** will not compile */
//    def foo[F[_, _], B](functor: Functor[F[B, ?]]) = ???
  }

  object solution21 {
    type A1[A] = Map[Int, A]
    def foo[F[_, _], B](f: Functor[A1]) = ???
  }

  object solution22 {
    def foo2[F[_, _], B](f: Functor[({ type ab[C] = F[B, C] })#ab]) = ???
  }

  object solution23 {
    def foo2[F[_, _], B](f: Functor[F[B, *]]) = ???
  }

  object problem3 {

    /** we can't write */
//    def makeFunctor[A]: Functor[Map[Int, A]] = new Functor[Map[Int, A]]] {}
  }

  object solution31 {
    def makeFunctor[A] = new Functor[({ type f[x] = Map[A, x] })#f] {}
  }

  object solution32 {
    def makeFunctor[A] = new Functor[Map[A, *]] {}
  }

  val f1 = solution31.makeFunctor[Int]
  val f2: Functor[({ type f[x] = Map[Int, x] })#f] = solution31.makeFunctor[Int]
  val f3: Functor[Map[Int, *]] = solution31.makeFunctor[Int]

  /** we set 1st type parameter and Functor has no information about it
    *
    * Functor will operate with, Map[Int, _] as F[_]
    */

  lazy val z1: Map[Int, Int] = f3.as(1)
  lazy val z2: Map[Int, Boolean] = f3.as(true)
  lazy val z3: Map[Int, String] = f3.as[String]("Hell")

  /** we can erase type intentionally */
  type MapK[K] = Map[K, _]
  val mapk: MapK[Int] = Map(1 -> 1)
  val value: Option[_] = mapk.get(1)
}
