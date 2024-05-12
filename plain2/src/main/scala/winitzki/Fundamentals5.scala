package winitzki

import scala.annotation.tailrec

object Fundamentals5 {

  trait Universe

  trait Animal extends Universe
  trait Dog    extends Animal
  trait Cat    extends Animal

  class Dog1 extends Dog
  class Dog2 extends Dog

  class Cat1 extends Cat
  class Cat2 extends Cat

  case class Box[A](a: A)
  case class BoxCo[+A](a: A)
  case class BoxContra[A >: Dog1](a: A)

  // invariant
  Box[Dog](new Dog {})
  Box[Dog1](new Dog1)

  Box[Dog](new Dog1)
  Box[Animal](new Dog1)

  // doesn't work
//  Box[Dog1](new Dog{})
//  Box[Dog](new Animal {})

  BoxContra(new Dog {})
  BoxContra(new Dog1)

}

// page 121
class Fundamentals5 extends Base {

  import Fundamentals5._

  test("perfectly shaped binary tree definition") {
    sealed trait PTree[A]
    final case class Leaf[A](x: A)                extends PTree[A]
    final case class Branch[A](xs: PTree[(A, A)]) extends PTree[A]

    /** functor is very natural and easy to implement */
    def map[A, B](t: PTree[A])(f: A => B): PTree[B] = t match {
      case Leaf(x)    => Leaf(f(x))
      case Branch(xs) => Branch(map(xs) { case (x, y) => (f(x), f(y)) })
    }

  }

  test("converge 1") {
    def converge[X](f: X => X, x0: X, cond: X => Boolean): X = {
      def go(x: X): X = f(x) match {
        case x1 if cond(x1) => x1
        case x1             => go(x1)
      }
      go(x0)
    }
  }

  test("converge 2") {
    @tailrec
    def converge[X](f: X => X, x0: X, cond: X => Boolean): X =
      cond(x0) match {
        case true => x0
        case _    => print("."); converge(f, f(x0), cond)
      }

    // precision - steps
    // 1e-12     -> 4
    // 1e-8      -> 3
    def newtonSqrt(q: Double, precision: Double = 1.0e-12): Double = {
      def cond(x: Double): Boolean = math.abs(x * x - q) <= precision
      def iterate(x: Double): Double = 0.5 * (x + q / x)
      converge(iterate, q / 2, cond)
    }

    val precise: Double = math.sqrt(3)
    val approx: Double = newtonSqrt(3)
    println()
    val diff = math.abs(precise - approx)

    pprint.log(precise)
    pprint.log(approx)
    pprint.log(diff)

    diff should be < 1.0e-8

  }

  test("converge 3") {
    def converge[X](f: X => X, x0: X, cond: X => Boolean): X =
      LazyList
        .iterate(x0)(f)
        .find(cond)
        .getOrElse(throw new IllegalArgumentException())
  }

  test("compositions") {
    // identity. return whatever given
    def id[A]: A => A = (a: A) => a
    def const[A](a: A): Any => A = (_: Any) => a

//    id(id)       === id
//    id(id)(id)   === id
//    id(id(id))   === id
//    id(const)    === const
//    const(const) === _ => const
//    const(id)    === _ => id
  }

  test("flip") {

    def flip[A, B, C](f: (A, B) => C): (B, A) => C =
      (b: B, a: A) => f(a, b)

    val f = (a: Int, b: Double) => s"$a:$b"
    val g: (Double, Int) => String = flip(f)

    f(1, 2.5) shouldBe g(2.5, 1)

  }

  // https://en.wikipedia.org/wiki/Hindley%E2%80%93Milner_type_system
  // http://dysphoria.net/2009/06/28/hindley-milner-type-inference-in-scala/
  test("derivation") {
    // having           can derive
    // ------------------------------------
    // A => B           Option[A] => Option[B]
    // A => B, B => C   A => C
  }

  test("5.1.2 / six standard type constructions") {
    // 1. primitive + Unit + Nothing
    //    - () can be constructed from anything, since it doesn't contain a data
    //    - we call unit `1`
    //    - named Unit is a case class NUnit()
    //    - CH(Nothing) is always false, no values of Nothing
    //    - Nothing is called `0`
    //    - CH(primitive) is always true
    // 2. product
    //    - CH((A, B)) = CH(𝐴) ∧ CH(𝐵), the same for case classes
    //    - CH(A × B × ... × C) = CH(A) ∧ CH(B) ∧ ... ∧ CH(C)
    // 3. co-product
    //    Option[A]   === 1 + A
    //    Either[A,B] === A + B
    //    case object NoRoots                         extends Solution
    //    case class OneRoot(x: Double)               extends Solution
    //    case class TwoRoots(x1: Double, x2: Double) extends Solution
    //    Solution === 1 + Double + Double × Double
    //                 --  ------   ---------------
    //                 no   one           two
    //  flatMap 𝐴,𝐵 :1+𝐴→(𝐴→1+𝐵)→1+𝐵]
    def flatMap[A, B](oa: Option[A], f: A => Option[B]): Option[B] = oa match {
      case Some(a) => f(a)
      case None    => None
    }
    // 4. function types
    //      - CH(A => B) = CH(A) => CH(B)
    //       A => Option[B] denoted A => 1 + B
    // 5. parameterized types
    def f[A, B]: A => (A => B) => B = x => g => g(x)
    // 6. recursive types
  }

  test("5.2.2 eight standard code constructions") {
    def f[A, B, X](a: A, b: B): X = { // Any given type signature.
      val x1: Unit = ()               // 1) Use a value of type Unit.
      val x2: A = a                   // 2) Use a given argument.
      val x3: A => B = { x: A => b }  // 3) Create a function.
      val x4: B = x3(x2)              // 4) Use a function.
      val x5: (A, B) = (a, b)         // 5) Create a tuple.
      val x6: B = x5._2               // 6) Use a tuple.
      val x7: Option[A] = Some(x2)    // 7) Create values of a disjunctive type.
      val x8 = x7 match {
        case Some(value) => ???
        case None        => ???
      }                               // 8) Use values of a disjunctive type.
      ???
      // 9) Call f() itself recursively. Not included here because not supported by CH-propositions.
    }
  }

  test("Exercise 5.1.4.1") {
    sealed trait Q[T, A]
    case class Q1()                      extends Q[Nothing, Nothing]
    case class Q2[T, A](t: T, a: A)      extends Q[T, A]
    case class Q3[T](x: Int, tf: T => T) extends Q[T, Nothing]
    case class Q4[A](x: String, a: A)    extends Q[Nothing, A]
  }

  test("p142") {}

  test("146") {
    def f[A](x: Option[Option[A]]) = x match {
      case Some(Some(a)) => ???
      case Some(None)    => ???
      case None          => ???
    }
    // type equivalence
    def f1[A]: Option[A] => Either[Unit, A] = {
      case Some(a) => Right(a)
      case None    => Left(())
    }
    def f2[A]: Either[Unit, A] => Option[A] = {
      case Right(a) => Some(a)
      case Left(_)  => None
    }
  }

  test("5.4.2.7 / p163") {
    // OptE = 1 + 𝑇 + 𝐴
    sealed trait OE[+T, +A] // +T gives ability to use Nothing
    case class OE1()        extends OE[Nothing, Nothing]
    case class OE2[T](t: T) extends OE[T, Nothing]
    case class OE3[A](a: A) extends OE[Nothing, A]

    // map𝐴,𝐵,𝑇 : OptE𝑇,𝐴 → (𝐴 → 𝐵) → OptE𝑇,𝐵
    def map[A, B, T](ot: OE[T, A])(f: A => B): OE[T, B] =
      ot match {
        case OE1()  => OE1()
        case OE2(t) => OE2(t)
        case OE3(a) => OE3(f(a))
      }
    // flatMap𝐴,𝐵,𝑇 :OptE𝑇,𝐴→(𝐴→OptE𝑇,𝐵)→OptE𝑇,𝐵
    def flatMap[A, B, T](ot: OE[T, A])(f: A => OE[T, B]): OE[T, B] =
      ot match {
        case OE1()  => OE1()
        case OE2(t) => OE2(t)
        case OE3(a) => f(a)
      }
  }

  test("5.4.2.8 / p163") {
    sealed trait P[A]
    final case class P1[A]()               extends P[A]
    final case class P2[A](x: A)           extends P[A]
    final case class P3[A](n: Int, x: A)   extends P[A]
    final case class P4[A](f: String => A) extends P[A]

    def map[A, B](pa: P[A])(f: A => B): P[B] =
      pa match {
        case P1()     => P1()
        case P2(x)    => P2(f(x))
        case P3(n, x) => P3(n, f(x))
        case P4(fsa)  => P4(s => f(fsa(s)))
      }
  }

  test("5.4.2.9 / p163") {
    sealed trait Q[+T, +A]
    case class Q1()                      extends Q[Nothing, Nothing]
    case class Q2[T, A](t: T, a: A)      extends Q[T, A]
    case class Q3[T](x: Int, tf: T => T) extends Q[T, Nothing]
    case class Q4[A](x: String, a: A)    extends Q[Nothing, A]

    // map𝑇,𝐴,𝐵 :𝑄𝑇,𝐴→(𝐴→𝐵)→𝑄𝑇,𝐵
    def map[T, A, B](q: Q[T, A], f: A => B): Q[T, B] =
      q match {
        case Q1()      => Q1()
        case Q2(t, a)  => Q2(t, f(a))
        case Q3(x, tf) => Q3(x, tf)
        case Q4(x, a)  => Q4(x, f(a))
      }

  }

}
