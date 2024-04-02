package cats101.c114reader

import cats.Monad
import cats.implicits.toFlatMapOps

object Reader0PlainComposition {

  def plus1(x: Int) = x + 1
  def x2(x: Int) = x * 2

  val a = 5
  val d: (Int, Int) = (x2(a), plus1(a))

  /** passing value to both functions */
  def composition1[A, B, C](f: A => B, g: A => C): A => (B, C) =
    a => (f(a), g(a))

  /** composing to the result */
  def composition2a[A, B, C](f: A => B, g: B => C): A => C =
    a => g(f(a))

  /** prepending the computation */
  def composition2b[A, B, AA](f: A => B, g: AA => A): AA => B =
    aa => f(g(aa))

  /** composition inside the context, basic syntax 1 */
  def composition3a[F[_], A, B, C](f: A => F[B], g: B => F[C])(implicit mf: Monad[F]): A => F[C] =
    a => {
      val fb = f(a)
      mf.flatMap(fb)(g)
    }

  /** composition inside the context, basic syntax 2 */
  def composition3b[F[_]: Monad, A, B, C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => {
      val mf = implicitly[Monad[F]]
      val fb = f(a)
      mf.flatMap(fb)(g)
    }

  /** composition inside the context, syntax by `cats.implicits.toFlatMapOps`  */
  def composition3c[F[_]: Monad, A, B, C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => {
      val fb = f(a)
      fb.flatMap(g)
    }

  /** composition inside the context, syntax by `betterMonadicFor` */
  def composition3d[F[_]: Monad, A, B, C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => {
      val fb = f(a)
      F.flatMap(fb)(g)
    }

}
