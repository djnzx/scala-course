package fp_red.red12

import fp_red.red05.Stream
import fp_red.red10.Monoid
import fp_red.red11.Functor

import scala.language.implicitConversions

/**
  * applicative functor.
  * to implement map -> we need to implement map2
  * to implement map2 -> we need to implement apply
  *
  * So, to implement it we need to implement:
  * map2 or apply
  * */
trait Applicative[F[_]] extends Functor[F] { self =>

  /** primitive #1. map2 via apply */
  def map2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = {
    val fc: A => (B => C) = f.curried
    val fbc: F[B => C] = map(fa)(fc)
    apply(fbc)(fb)
  }

  /** primitive #2. add the layer "F" */
  def unit[A](a: => A): F[A]

  /** `apply` via `map2` */
  def apply[A,B](fab: F[A => B])(fa: F[A]): F[B] =
    map2(fa, fab) { (fa: A, fab: A => B) => fab(fa) }

  /** `map` via `unit` and `apply` */
  def map_functor[A,B](fa: F[A])(f: A => B): F[B] =
    apply(unit(f))(fa)

  /** `map` can be expressed via different combinators:
    * `unit` and `map2`
    */
  def map[A,B](fa: F[A])(f: A => B): F[B] =
    map2(fa, unit())((a, _) => f(a))

  def map3[A,B,C,D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] =
    apply(
      apply(
        apply(
          unit(f.curried)
        )(fa)
      )(fb)
    )(fc)
  
  /** `traverse` via `map2` and `unit` */
  def traverse[A,B](as: List[A])(f: A => F[B]): F[List[B]] =
    as.foldRight(unit(List.empty[B])) { (a: A, flb: F[List[B]]) =>
      map2(f(a), flb)(_ :: _)
    }
    
  /** `sequence` via `traverse` */
  def sequence[A](fas: List[F[A]]): F[List[A]] = 
    traverse(fas) { fa: F[A] => fa }

  /** `replicateM` via `sequence` */
  def replicateM[A](n: Int, fa: F[A]): F[List[A]] =
    sequence(List.fill(n)(fa))

  /** `product` via `map2` */
  def factor[A,B](fa: F[A], fb: F[B]): F[(A,B)] =
    map2(fa, fb)((_, _))

  def product[G[_]](G: Applicative[G]): Applicative[({type f[x] = (F[x], G[x])})#f] =
    new Applicative[({type f[x] = (F[x], G[x])})#f] {
      override def unit[A](a: => A): (F[A], G[A]) = 
        (self.unit(a), G.unit(a))

      override def apply[A, B](fab: (F[A => B], G[A => B]))(fa: (F[A], G[A])): (F[B], G[B]) = { 
        (fab, fa) match {
          case ((fab, gab), (fa, ga)) => (self.apply(fab)(fa), G.apply(gab)(ga))
        }
      }
    }

  def compose[G[_]](G: Applicative[G]): Applicative[({type f[x] = F[G[x]]})#f] =
    new Applicative[({type f[x] = F[G[x]]})#f] {
      override def unit[A](a: => A): F[G[A]] = {
        self.unit(G.unit(a))
      }

      override def map2[A, B, C](fga: F[G[A]], fgb: F[G[B]])(f: (A, B) => C): F[G[C]] = {
        // strip the F layer
        self.map2(fga, fgb) { (ga: G[A], gb: G[B]) =>
          // strip the G layer
          G.map2(ga, gb) { (a: A, b: B) => 
            // apply the function
            f(a, b)
          }
        }
      }
    }

  def sequenceMap[K, V](ofa: Map[K, F[V]]): F[Map[K, V]] = 
    ofa.foldLeft(unit(Map.empty[K, V])) { case (fmkv, (k, fv)) =>
      map2(fmkv, fv) { case (mkv, v) => mkv + (k -> v) }
    }
}

case class Tree[+A](head: A, tail: List[Tree[A]])

/**
  * here are the applicatives examples,
  * but not a monads
  */
object Applicative {

  /**
    * Infinite Stream.
    * 
    * no way to implement flatMap to operate with infinite stream
    */
  val streamApplicative: Applicative[Stream] = new Applicative[Stream] {
    def unit[A](a: => A): Stream[A] = Stream.constant(a)

    override def map2[A,B,C](a: Stream[A], b: Stream[B])(f: (A, B) => C): Stream[C] = {
      val sab: Stream[(A, B)] = a zip b
      val ft: ((A, B)) => C = f.tupled
      val sc: Stream[C] = sab map ft
      sc
    }
  }

  def validationApplicative[E]: Applicative[({ type f[x] = Validation[E,x] })#f] =
    new Applicative[({ type f[x] = Validation[E, x] })#f] {
      override def unit[A](a: => A): Validation[E, A] = Success(a)

      override def map2[A, B, C](fa: Validation[E, A], fb: Validation[E, B])(f: (A, B) => C): Validation[E, C] =
        (fa, fb) match {
          case (Success(a), Success(b)) => Success(f(a,b))
          case (Failure(hl, tl), Failure(hr, tr)) => Failure(hl, tl ++ (hr +: tr))
          case (e @ Failure(_, _), _) => e
          case (_, e @ Failure(_, _)) => e
        }
    }

  /**
    * turning Monoid into Applicative 
    */
  type Const[M, B] = M
  /**
    * `map2[A, B, C](fa: Const[M, A], fb: Const[M, B])(f: (A, B) => C): Const[M, C]`
    * 
    * becomes 
    * 
    * `map2[A, B, C](fa: M, fb: M)(f: (A, B) => C): M`
    */
  implicit def monoidApplicative[M](M: Monoid[M]) =
    new Applicative[({ type f[x] = Const[M, x] })#f] {
      def unit[A](a: => A): M = M.zero
      override def apply[A, B](m1: M)(m2: M): M = M.op(m1, m2)
      override def map2[A, B, C](fa: M, fb: M)(f: (A, B) => C): M = M.op(fa, fb)
    }
}
