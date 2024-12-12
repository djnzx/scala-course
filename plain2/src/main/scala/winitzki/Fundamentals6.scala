package winitzki

import cats._
import cats.implicits._
import scala.concurrent.duration._
import scala.reflect.runtime.universe._

// functors and contrafunctors
// page 170
class Fundamentals6 extends Base {

  /** The most known data wrappers (functors)
    * - collections/streams: Array / Seq / List / Vector / Set / Map / Stream / LazyList
    * - Option / Either / Try
    * - Future / IO / ZIO / Task
    * - Validated
    * - Parser
    * - State
    * - Ref
    * - Id
    */

  test("6.1.2") {

    def mapY[A, B](oa: Option[A])(f: A => B): Option[B] =
      oa match {
        case Some(x) => Some(f(x))
        case None    => None
      }

    val x = Some(3.5)
    mapY(x)(identity) shouldBe x

    // lifting function to functor
    def fmap[A, B](f: A => B): Option[A] => Option[B] = {
      case Some(x) => Some(f(x))
      case None    => None
    }

  }

  test("6.1.3 / composition law of functors") {
    val c: Option[Int] = Some(5)

    val f = (x: Int) => x + 2
    val g = (x: Int) => x * 2

    c.fmap(f).fmap(g) shouldBe c.fmap(f andThen g)
    c.fmap(f).fmap(g) shouldBe c.fmap(g compose f)
  }

  test("6.1.4 / identity law") {
    val c: Option[Int] = Some(5)

    c.fmap(identity) shouldBe c
  }

  test("6.1.4.3") {
    sealed trait QueryResult[A]
    case class Error[A](message: String)                     extends QueryResult[A]
    case class Success[A](name: String, time: Long, data: A) extends QueryResult[A]

    def fmap[A, B](f: A => B): QueryResult[A] => QueryResult[B] = {
      case Error(message)            => Error(message)
      case Success(name, time, data) => Success(name, time, f(data))
    }
  }

  test("6.1.4.4 / list of odd elements") {
    // recursive polynomial functor
    sealed trait LO[A]
    final case class LO1[A](x: A)                    extends LO[A]
    final case class LO2[A](x: A, y: A, tail: LO[A]) extends LO[A]

    def fmap[A, B](f: A => B): LO[A] => LO[B] = {
      case LO1(x)          => LO1[B](f(x))
      case LO2(x, y, tail) => LO2[B](f(x), f(y), fmap(f)(tail))
    }
  }

  test("6.1.6 / not a functor") {
    final case class H[A](r: A => Int)

    // map 𝐴,𝐵: (A => Int) => (A => B) => (B => Int)

    def fmap[A, B](h: H[A])(f: A => B): H[B] = sys.error("impossible!")

    sealed trait ServerAction[R]
    final case class StoreId(x: Long, y: String) extends ServerAction[Boolean]
    final case class StoreName(name: String)     extends ServerAction[Int]
    // type R can't be used !
  }

  test("compare types") {
    // we can compare types
    // WeakTypeTag vs TypeTag
    def getType[A: WeakTypeTag]: Type = weakTypeOf[A]
    def equalTypes[A: WeakTypeTag, B: WeakTypeTag]: Boolean = getType[A] =:= getType[B]

    pprint.log(equalTypes[Int, Int])
    pprint.log(equalTypes[Int, Double])

    trait Animal
    trait Cat extends Animal
    pprint.log(equalTypes[Cat, Animal])

  }

  test("6.1.7 / contrafunctor") {
    final case class H[A](r: A => Int)

    // contramap𝐴,𝐵 :(𝐴→Int)→(𝐵→𝐴)→𝐵→Int

    def contramap[A, B](h: H[A])(f: B => A): H[B] =
      H(f andThen h.r)

    // use currying !!!
    def cmap[A, B](f: B => A) = (h: H[A]) =>
      H(f andThen h.r)

    // identity, composition as well
  }

  test("6.1.7.1") {
    def contramap[A, B](d: A => (A => Int))(f: B => A): B => B => Int =
      (b1: B) =>
        (b2: B) => {
          val a1: A = f(b1)
          val a2: A = f(b2)
          d(a1)(a2)
        }
    // functor       -> wrapped
    // contrafunctor -> consumed
  }

  test("Type constructors that are not contrafunctors") {
    // 𝑁 = (𝐴→Int) × (1+𝐴)
    case class N[A](f: A => Int, oa: Option[A])
    //                 consumed      wrapped
  }

  test("6.1.8 / covariance, contravariance, subtyping") {

    /** here we don't have any relation between P and Q */
    class Playground1[P, Q, Z] {
      def h(q: Q): Z = ???
      val p: P = ???

      // P is called a subtype of a type Q
      // if there exists
      /** so we provide a function to convert, compiler will attach it */
      implicit val pToQ: P => Q = ???

      val z: Z = h(p)
    }

    /** we use notation to say that
      *
      * - P is a subtype of Q
      * {{{P extends Q}}}
      * {{{P <: Q}}}
      */
    class Playground2[Q, P <: Q, Z] {
      //                 ======
      def h(q: Q): Z = ???
      lazy val p: P = ???
      lazy val z: Z = h(p)
    }

    /** then we can use it */
    new Playground2[Animal, Cat, String]
    new Playground2[Animal, Nothing, String]
    new Playground2[Animal, Null, String]
//    new Playground2[Cat, Dog, String] // doesn't compile

    // practical case 1
    trait Animal
    trait Cat extends Animal
    trait Dog extends Animal

    def h1(a: Animal): Int = 1

    // Cat is subtype of Animal
    // P extends Q (native type widening and erasure details)
    // it has all the properties of Animal + some extra
    // subtuping works automatically, thanks to `extends`
    val cat: Cat = new Cat {}
    h1(cat)

    // automatic conversion in cases:
    // 1. Declaring a class that “extends” another class.
    // 2. Declaring type parameters with a “variance annotation” such as L[+A] or L[-B].
    // 3. Declaring type parameters with a “subtyping annotation” (A <: B).

    /*
      if L[A] is a functor
      and we have f: P => Q
      we can lift `f` to
      L[P] => L[Q]
      which means L[P] <: L[Q]

      if H[A] is a contrafunctor
      and we have f: P => Q
      we can lift `f` to
      H[Q] => H[P]

      F[_] is covariant
      if F[A] is a subtype of F[B] whenever A is a subtype of B
      A <: B => F[A] <: F[B]
      if A extends B then F[A] extends F[B]

      H[_] is contravariant
      if H[B] is a subtype of H[A]

      - all functors are covariant type constructors            F[+A]
      - all contrafunctors are contravariant type constructors  F[-A]

      - F[A] is always subset of F[Nothing]
      - “consumer of a consumer of 𝐴” is covariant in 𝐴
     */

  }

  test("6.1.9.3a / contravariant") {

    type Data[-A] = Either[
      Option[A] => Int,
      A => ((A, A)) => String
    ]

    // `A` is the left side, consuming => contra

    def cmap[A, B](da: Data[A])(fba: B => A): Data[B] =
      da match {
        case Left(foaint)   => Left(ob => foaint(ob.map(fba)))
        case Right(fa2astr) => Right((b: B) => { case (b1, b2) => fa2astr(fba(b))(fba(b1) -> fba(b2)) })
      }

  }

  test("6.1.9.3b / p191") {
    /*
      type Data[A, B] = (Either[A, B], (A => Int) => B)
      - Either[A, B] - covariant in `A`
      - A => Int        - contravariant in `A`
      - (A => Int) => B - covariant in `A`

      - Either[A, B] - covariant in `B`
      - (A => Int) => B - covariant in `B`
     */
    type Data[A, B] = (Either[A, B], (A => Int) => B)

    def fmap[A, B, Z](d: Data[A, Z])(faz: A => B): Data[B, Z] = d match {
      case (eab, faintb) =>
        val newEab: Either[B, Z] = eab match {
          case Left(a)  => Left(faz(a))
          case Right(b) => Right(b)
        }

        val newFaIb: (B => Int) => Z =
          fZint => {
            val faint: A => Int = (a: A) => fZint(faz(a))
            val b: Z = faintb(faint)
            b
          }

        newEab -> newFaIb
    }

  }

  test("6.1.9.3b / covariant - small but interesting part") {

    type F[+A, +Y] = (A => Int) => Y

    def fmapA[A, B, Y](fainty: F[A, Y])(fab: A => B): F[B, Y] =
      fbint => fainty(a => fbint(fab(a)))

    def fmapY1[A, Y, Z](fainty: F[A, Y])(fab: Y => Z): F[A, Z] =
      faint => fab(fainty(faint))

    def fmapY2[A, Y, Z](fainty: F[A, Y])(fab: Y => Z): F[A, Z] =
      fainty andThen fab

  }

  test("6.1.9.4") {
    sealed trait Coi[+A, B]
    final case class Pa[A, B](ab: (A, B), fbint: B => Int)   extends Coi[A, B]
    final case class Re[A, B](a: A, b: B, int: Int)          extends Coi[A, B]
    final case class Ci[A, B](fsa: String => A, fba: B => A) extends Coi[A, B]

    sealed trait Coi2[+A, B, +N, -S]
    case class Pa2[+A, B, +N, -S](ab: (A, B), fbn: B => N)  extends Coi2[A, B, N, S]
    case class Re2[+A, B, +N, -S](a: A, b: B, n: N)         extends Coi2[A, B, N, S]
    case class Ci2[+A, B, +N, -S](fsa: S => A, fba: B => A) extends Coi2[A, B, N, S]

    // A - covariant
    def mapA[A, B, C](coi: Coi[A, B])(f: A => C): Coi[C, B] = coi match {
      case Pa(ab, fbint) => Pa(f(ab._1) -> ab._2, fbint)
      case Re(a, b, int) => Re(f(a), b, int)
      case Ci(fsa, fba)  => Ci(fsa andThen f, fba andThen f)
    }

    // B - not covariant, not contravariant
  }

  test("6.1.10.3") {
    sealed trait S[A, +B]
    final case class P[A, B](a: A, b: B, c: Int)       extends S[A, B]
    final case class Q[A, B](d: Int => A, e: Int => B) extends S[A, B]
    final case class R[A, B](f: A => A, g: A => B)     extends S[A, B]
    // A - co/contra => invariant
    // B - wrapper / right side => covariant
  }

  test("6.2") {
    // functor identity
//    x.map(identity) == x
    // functor composition
//    x.map(f andThen g) == x.map(f).map(g)
  }

  test("6.2.2") {
    // bifunctor - just two-hole functor
  }

  test("6.2.3.2 - const") {
    type Const[Z, A] = Z

    def fmap[A, B, Z](f: A => B): Const[Z, A] => Const[Z, B] = identity[Z]
  }

  test("6.2.3.2 - product") {
    def fmap[A, B, L[_]: Functor, M[_]: Functor](f: A => B): (L[A], M[A]) => (L[B], M[B]) = {
      case (la, ma) => (la.map(f), ma.map(f))
    }
  }

  test("6.2.3.3 - coproduct") {
    def fmap[A, B, P[_]: Functor, Q[_]: Functor](f: A => B): Either[P[A], Q[A]] => Either[P[B], Q[B]] = {
      case Left(pa)  => Left(Functor[P].fmap(pa)(f))
      case Right(qa) => Right(Functor[Q].fmap(qa)(f))
    }
  }

  test("6.2.3.5 - exponential - applying one by one - understanding") {

    def fmap[A, B, C[_]: Contravariant, P[_]: Functor](f: A => B)(h: C[A] => P[A]): C[B] => P[B] =
      (cb: C[B]) => {
        // pre-composition
        val ca: C[A] = Contravariant[C].contramap(cb)(f)
        // apply h
        val pa: P[A] = h(ca)
        // post-composition
        val pb: P[B] = Functor[P].fmap(pa)(f)

        pb
      }
  }

  test("6.2.3.5 - exponential - exploring things") {

    def fmap[A, B, C[_]: Contravariant, P[_]: Functor](f: A => B)(h: C[A] => P[A]): C[B] => P[B] = {
      // pre-composition
      val `c[b] => c[a]` = (cb: C[B]) => Contravariant[C].contramap(cb)(f)

      // post-composition
      val `p[a] => p[b]` = (pa: P[A]) => Functor[P].fmap(pa)(f)

      // via natural application - hard
      val full = (cb: C[B]) => `p[a] => p[b]`(h(`c[b] => c[a]`(cb)))

      // full composition via compose - better, but still hard
      `p[a] => p[b]` compose h compose `c[b] => c[a]`

      // full composition via profunctor - better
      h.dimap(`c[b] => c[a]`)(`p[a] => p[b]`)

      // full composition in a natural way - nice
      `c[b] => c[a]` andThen h andThen `p[a] => p[b]`

      // full composition in an arrow syntax - perfect
      `c[b] => c[a]` >>> h >>> `p[a] => p[b]`
    }

  }

  test("6.2.3.6") {

    def fmap[A, B, P[_]: Functor, Q[_]: Functor](f: A => B): P[Q[A]] => P[Q[B]] = {
      // getting functor inside Q
      val `q[a] => q[b]` = (qa: Q[A]) => Functor[Q].fmap(qa)(f)
      // applying inside P
      (pqa: P[Q[A]]) => Functor[P].fmap(pqa)(`q[a] => q[b]`)
    }

    // Semigroup[Long]
    val combinerLong: Semigroup[Long] = Semigroup[Long]

    // we know how
    def longToDuration: Long => FiniteDuration = Duration.fromNanos

    // we also know
    def durationToLong: FiniteDuration => Long = _.toNanos

    // now we can derive Semigroup[FiniteDuration] (profunctor)
    val combiner: Semigroup[FiniteDuration] = Invariant[Semigroup].imap(combinerLong)(longToDuration)(durationToLong)

    // it takes type A, converts to B, does B |+| B, converts to A back
    val combined1: FiniteDuration = combiner.combine(2.seconds, 3.seconds)
    val combined2: FiniteDuration = 2.seconds |+| 3.seconds

    sealed trait List[A]
    final case class Empty[A]()                      extends List[A]
    final case class Head[A](head: A, tail: List[A]) extends List[A]

    sealed trait Tree2[A]
    final case class Leaf[A](a: A)                       extends Tree2[A]
    final case class Branch[A](x: Tree2[A], y: Tree2[A]) extends Tree2[A]

    sealed trait TreeN[A]
    final case class LeafN[A](a: A)                extends TreeN[A]
    final case class BranchN[A](xs: Seq[TreeN[A]]) extends TreeN[A]

  }

  test("50 shades of composition") {
    // 1. our function (x => x * 10)
    val x10: Int => Int = x => x * 10
    // 2. our prefix function
    val prefix = (x: Double) => x.ceil.toInt

    // compose #0 in terms of scala syntax
    val combo0: Double => Int = x => x10(prefix(x))
    // compose #1 in terms of andThen standard library
    val combo1: Double => Int = prefix andThen x10
    // compose #3 in terms of andThen standard library
    val combo2: Double => Int = x10 compose prefix
    // compose #2 in terms of cats.arrow.Compose[F[_, _]]
    val combo3: Double => Int = prefix >>> x10
    // compose #4 in terms of cats.arrow.Compose[F[_, _]]
    val combo4: Double => Int = x10 <<< prefix
    // compose #5 in terms of cats.arrow.Profunctor[F[_, _]]
    val combo5: Double => Int = x10.lmap(prefix)
    // compose #6 in terms of cats.arrow.Profunctor[F[_, _]]
    val combo6: Double => Int = prefix.rmap(x10)

    // compose #7 in terms of cats.Functor[F[_]] - andThen
    type F[A] = Double => A
    // F[Int] means "It will provide an Int, eventually"
    val combo7: F[Int] = Functor[F].fmap(prefix)(x10)

    // compose #8 in terms of cats.Contravariant[F[_]] - compose
    type G[A] = A => Int
    // G[Int] means "It will consume an Int"
    val x10g: G[Int] = x => x * 10
    // G[Double] means "I will consume a Double" (after composition)
    val combo8: G[Double] = Contravariant[G].contramap(x10g)(prefix)

    // initially it was consuming an Int (f: G[Int] = x => x * 10)
    // after providing f: Double => Int, it will be consuming a Double
    // `G[Int]` becomes `G[Double]`
    // but due to the contravariance semantics it looks vise versa:
    // when `G[Int]` is consumed - `G[Double]` is emitted
    val gg11: G[Int] => G[Double] = (g: G[Int]) => Contravariant[G].contramap(g)(prefix)
    val gg12: G[Int] => F[Int] = (g: G[Int]) => Contravariant[G].contramap(g)(prefix)

    /** compiler can be used af a proof of our assumptions! */
    val gg21: F[Int] => F[Int] = (fba: F[Int]) => Contravariant[G].contramap(x10g)(fba)
    val gg22: F[Int] => G[Double] = (fba: F[Int]) => Contravariant[G].contramap(x10g)(fba)
    val gg23: G[Double] => F[Int] = (fba: F[Int]) => Contravariant[G].contramap(x10g)(fba)
    val gg24: G[Double] => G[Double] = (fba: F[Int]) => Contravariant[G].contramap(x10g)(fba)

    val gg25: F[Int] => F[Int] = (fba: G[Double]) => Contravariant[G].contramap(x10g)(fba)
    val gg26: F[Int] => G[Double] = (fba: G[Double]) => Contravariant[G].contramap(x10g)(fba)
    val gg27: G[Double] => F[Int] = (fba: G[Double]) => Contravariant[G].contramap(x10g)(fba)
    val gg28: G[Double] => G[Double] = (fba: G[Double]) => Contravariant[G].contramap(x10g)(fba)

    // assertions
    Seq(
      combo0,
      combo1,
      combo2,
      combo3,
      combo4,
      combo5,
      combo6,
      combo7,
      combo8,
      gg11(x10g),
      gg12(x10g),
      gg21(prefix),
      gg22(prefix),
      gg23(prefix),
      gg24(prefix),
      gg25(prefix),
      gg26(prefix),
      gg27(prefix),
      gg28(prefix),
    )
      .foreach(f => f(3.5) shouldBe 40)
  }

  test("arrow, compose, category") {
    val f: Int => Int = _ + 1
    val g: Int => Int = _ * 100

    val h1: Int => Int = f >>> g // f andThen g
    h1(3) shouldBe 400

    val h2: Int => Int = f <<< g // g andThen f
    h2(3) shouldBe 301
  }

  test("6.2.4 - p 204 ???") {
    type Const[Z, A] = Z
    def cmap[Z, A, B](f: B => A): Const[Z, A] => Const[Z, B] = identity[Z]
  }
}
