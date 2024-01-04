package ctbartoz

import cats._
import cats.arrow.Profunctor
import cats.data._
import cats.implicits.catsSyntaxAlternativeSeparate
import cats.implicits.catsSyntaxEitherId
import cats.implicits.catsSyntaxUnite
import cats.implicits.toBifunctorOps
import cats.implicits.toContravariantOps
import cats.implicits.toFunctorOps
import cats.implicits.toProfunctorOps
import cats.implicits.toTraverseOps
//import cats.implicits._

object CT101 extends App {

  /** https://www.youtube.com/playlist?list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_
    * 1.1 https://www.youtube.com/watch?v=I8LbkfSSR58&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=1
    * 1.2 https://www.youtube.com/watch?v=p54Hd7AmVFU&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=2
    * 2.1 https://www.youtube.com/watch?v=O2lZkr-aAqk&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=3
    * 2.2 https://www.youtube.com/watch?v=NcT7CGPICzo&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=4
    * 3.1 https://www.youtube.com/watch?v=aZjhqkD6k6w&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=5
    * 3.2 https://www.youtube.com/watch?v=i9CU4CuHADQ&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=6
    * 4.1 https://www.youtube.com/watch?v=zer1aFgj4aU&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=7
    * 4.2 https://www.youtube.com/watch?v=Bsdl_NKbNnU&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=8
    * 5.1 https://www.youtube.com/watch?v=LkIRsNj9T-8&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=10
    * 5.2 https://www.youtube.com/watch?v=w1WMykh7AxA&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=10
    * 6.1 https://www.youtube.com/watch?v=FyoQjkwsy7o&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=11
    * 6.2 https://www.youtube.com/watch?v=EO86S2EZssc&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=12
    * 7.1 https://www.youtube.com/watch?v=pUQ0mmbIdxs&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=13
    * 7.2 https://www.youtube.com/watch?v=wtIKd8AhJOc&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=14
    * 8.1 https://www.youtube.com/watch?v=REqRzMI26Nw&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=15
    * 8.2 https://www.youtube.com/watch?v=iXZR1v3YN-8&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_&index=16
    * 9.1
    * 9.2
    * 10.1
    * 10.2
    */

  class Associativity[A, B, C, D](
      f: A => B,
      g: B => C,
      h: C => D) {

    val composition1: A => D = (f andThen g) andThen h
    val composition2: A => D = f andThen (g andThen h)
  }

  object FlattenVsUnite {
    val xs: List[Vector[Int]] = List(Vector(1, 2), Vector(3, 4))
    // F[G[A] (FlatMap[F], Alternative[F], Foldable[G]) => F[A]
    // Alternative = Applicative + SemigroupK
    val r2: List[Int]         = xs.unite
    val r1: List[Int]         = xs.flatten // (implicit iterableOnce: A => IterableOnce[B])
  }

  object RepackFutures {
    import scala.concurrent.ExecutionContext
    import scala.concurrent.Future

    def repack[A](fs: List[Future[A]])(implicit ec: ExecutionContext): Future[(List[Throwable], List[A])] = {
      def refine(fa: Future[A]): Future[Either[Throwable, A]] = fa.map(_.asRight).recover(_.asLeft)

      fs.map(refine)
        .sequence
        .map(_.separate)
    }

  }

  def repackEithers[A, B](xs: List[Either[A, B]]): (List[A], List[B]) = xs.separate

  def repackTuples[A, B](xs: List[(A, B)]): (List[A], List[B]) = xs.separate

  val a = repackTuples(
    List(
      (1, "a"),
      (2, "b"),
      (3, "c"),
    )
  )
//  pprint.pprintln(a)

  val b = repackEithers(
    List(
      Left(1),
      Right("a"),
      Left(2),
      Right("b")
    )
  )
//  pprint.pprintln(b)

  object bi {
    val x: Either[String, Int]            = ???
    val y: Either[Option[Double], String] = x.bimap(_.toDoubleOption, _.toString)
  }

  object profunctor {

    def f(a: Int): Float = a.toFloat

    def pre(a: String): Int = a.length

    def post(a: Float): Double = a.toDouble

    val comb1: String => Double = (pre _) andThen f andThen post

    val comb2a: String => Double = (f _).dimap(pre)(post)
    val comb2b: String => Float  = (f _).dimap(pre)(identity)

    val comb3: Int => Double = (f _).map(post)

    val pf: Profunctor.Ops[Function, Int, Float] { type TypeClassType = Profunctor[Function] } = toProfunctorOps(f)

    type ToString[A] = A => String

    // cats contramap requires type constructor with one hole to derive contravariant
    val f10a                = (x: Int) => s"original: $x"
    val f10c: ToString[Int] = (x: Int) => s"original: $x"
    val f11                 = f10c.contramap((s: String) => s.length)

    val r: String = f11("ten")

    implicit def mkFunctionContravariant[C]: Contravariant[* => C] =
      new Contravariant[* => C] {
        override def contramap[A, B](fac: A => C)(fba: B => A): B => C =
          (b: B) => {
            val a: A = fba(b)
            val c: C = fac(a)
            c
          }
      }
  }

  pprint.pprintln(profunctor.r)
}
