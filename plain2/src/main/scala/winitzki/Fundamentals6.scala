package winitzki

import cats.implicits.toFunctorOps
import scala.reflect.runtime.universe._

// funcctors and contrafunctors
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
    // B - wrapper / right side => covariant
  }
}
