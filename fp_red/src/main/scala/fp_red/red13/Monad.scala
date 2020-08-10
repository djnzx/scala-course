package fp_red.red13

import scala.language.implicitConversions

trait Functor[F[_]] {
  def map[A,B](a: F[A])(f: A => B): F[B]
}

trait Monad[F[_]] extends Functor[F] {
  def unit[A](a: => A): F[A]
  def flatMap[A,B](a: F[A])(f: A => F[B]): F[B]

  def map[A,B](a: F[A])(f: A => B): F[B] = flatMap(a)(a => unit(f(a)))
  def map2[A,B,C](a: F[A], b: F[B])(f: (A,B) => C): F[C] = flatMap(a)(a => map(b)(b => f(a,b)))
  /** `foldLeft Stream[A]` to `F[B]` via `f:(B,A)=>F[B]` - `returns F[B]` */
  def foldM[A,B](l: Stream[A])(z: B)(f: (B,A) => F[B]): F[B] = l match {
    case h #:: t => f(z, h) flatMap (z2 => foldM(t)(z2)(f))
    case _ => unit(z)
  }
  /** `foldLeft Stream[A]` to `F[B]` via `f:(B,A)=>F[B]` - discard results */
  def foldM_[A,B](l: Stream[A])(z: B)(f: (B,A) => F[B]): F[Unit] = skip { foldM(l)(z)(f) }
  /** whatever given, replaces with given `b`, actually `unit` */
  def as[A,B](a: F[A])(b: B): F[B] = map(a)(_ => b) // or just unit(b) ???
  def skip[A](a: F[A]): F[Unit] = as(a)(())
  /** ??? */
  def foreachM[A](l: Stream[A])(f: A => F[Unit]): F[Unit] = foldM_(l)(())((u,a) => skip(f(a)))
  def sequence_[A](fs: Stream[F[A]]): F[Unit] = foreachM(fs)(skip)
  def sequence_[A](fs: F[A]*): F[Unit] = sequence_(fs.toStream)
  def replicateM[A](n: Int)(f: F[A]): F[List[A]] = Stream.fill(n)(f).foldRight(unit(List.empty[A]))(map2(_,_)(_ :: _))
  def replicateM_[A](n: Int)(f: F[A]): F[Unit] = foreachM(Stream.fill(n)(f))(skip)

  /** actually does the job and returns Boolean lifted to to `F[Boolean]` */
  def when[A](b: Boolean)(fa: => F[A]): F[Boolean] = if (b) as(fa)(true) else unit(false)

  def forever[A,B](a: F[A]): F[B] = {
    lazy val t: F[B] = a flatMap (_ => t)
    t
  }
  def while_(a: F[Boolean])(b: F[Unit]): F[Unit] = {
    lazy val t: F[Unit] = while_(a)(b)
    a flatMap (c => skip(when(c)(t)))
  }
  /** do `F[A]` while `cond==true` */
  def doWhile[A](fa: F[A])(cond: A => F[Boolean]): F[Unit] = for {
    a  <- fa
    ok <- cond(a)
    _  <- if (ok) doWhile(fa)(cond) else unit(())
  } yield ()
  /** andThen */
  def seq[A,B,C](f: A => F[B])(g: B => F[C]): A => F[C] = f andThen (fb => flatMap(fb)(g))

  /**
    * syntax for attaching methods directly to F[A]
    *
    * map(fa)(f) => fa.map(f)
    */
  implicit def toMonadic[A](a: F[A]): Monadic[F,A] =
    new Monadic[F,A] {
      override val F = Monad.this;
      override def get = a }
}

trait Monadic[F[_],A] {
  val F: Monad[F]
  def get: F[A]
  private val a = get
  def map[B](f: A => B): F[B] = F.map(a)(f)
  def flatMap[B](f: A => F[B]): F[B] = F.flatMap(a)(f)
  /** product */
  def **[B](b: F[B]) = F.map2(a,b)((_,_))
  /** flatMap w/ 1st result discarding */
  def *>[B](b: F[B]) = F.map2(a,b)((_,b) => b)
  def map2[B,C](b: F[B])(f: (A,B) => C): F[C] = F.map2(a,b)(f)
  def as[B](b: B): F[B] = F.as(a)(b)
  def skip: F[Unit] = F.skip(a)
  def replicateM(n: Int) = F.replicateM(n)(a)
  def replicateM_(n: Int) = F.replicateM_(n)(a)
}
