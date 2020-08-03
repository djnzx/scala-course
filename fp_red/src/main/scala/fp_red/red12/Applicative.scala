package fp_red.red12

import fp_red.red05.Stream
import fp_red.red06.State
import fp_red.red10.{Foldable, Monoid}
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
trait Applicative[F[_]] extends Functor[F] {

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

  def product[G[_]](G: Applicative[G]): Applicative[({type f[x] = (F[x], G[x])})#f] = ???

  def compose[G[_]](G: Applicative[G]): Applicative[({type f[x] = F[G[x]]})#f] = ???

  def sequenceMap[K,V](ofa: Map[K,F[V]]): F[Map[K,V]] = ???
}

case class Tree[+A](head: A, tail: List[Tree[A]])

/**
  * if we have monad, we don't need to define applicative `map2`,
  * we can define `flatMap` and `unit`.
  * All monads are applicative functors.
  */
trait Monad[F[_]] extends Applicative[F] {
  def flatMap[A,B](ma: F[A])(f: A => F[B]): F[B] = join(map(ma)(f))

  /** remove the layer "F" */
  def join[A](mma: F[F[A]]): F[A] = flatMap(mma)(ma => ma)

  def compose[A,B,C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => flatMap(f(a))(g)

  override def apply[A,B](mf: F[A => B])(ma: F[A]): F[B] =
    flatMap(mf)(f => map(ma)(a => f(a)))

  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => unit(f(a)))

  override def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
    flatMap(fa)(a => map(fb)(b => f(a, b)))
}

object Monad {
  def eitherMonad[E]: Monad[({type f[x] = Either[E, x]})#f] = ???

  def stateMonad[S] = 
    new Monad[({type f[x] = State[S, x]})#f] {
    def unit[A](a: => A): State[S, A] = State(s => (a, s))
    override def flatMap[A,B](st: State[S, A])(f: A => State[S, B]): State[S, B] =
      st flatMap f
  }

  def composeM[F[_],N[_]](implicit F: Monad[F], N: Monad[N], T: Traverse[N]): 
  Monad[({type f[x] = F[N[x]]})#f] = ???
}

sealed trait Validation[+E, +A]
case class Failure[E](head: E, tail: Vector[E] = Vector.empty) extends Validation[E, Nothing]
case class Success[A](a: A) extends Validation[Nothing, A]

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

  type Const[A, B] = A

  implicit def monoidApplicative[M](M: Monoid[M]) =
    new Applicative[({ type f[x] = Const[M, x] })#f] {
      def unit[A](a: => A): M = M.zero
      override def apply[A,B](m1: M)(m2: M): M = M.op(m1, m2)
    }
}

/**
  * functor +
  * Left and right identity
  * map(v)(id) == v
  * map(map(v)(g))(f) == map(v)(f compose g)
  * 
  * map2(unit(()), fa)((_,a) => a) == fa
  * map2(fa, unit(()))((a,_) => a) == fa
  * 
  * assoc:
  * op(a, op(b, c)) == op(op(a, b), c)
  * compose(f, op(g, h)) == compose(compose(f, g), h)
  * 
  * product(product(fa,fb),fc) == map(product(fa, product(fb,fc)))(assoc)
  * 
  * map2(a,b)(productF(f,g)) == product(map(a)(f), map(b)(g))
  */
object ApplicativeLaws {
  def assoc[A,B,C](p: (A,(B,C))): ((A,B), C) = p match { case (a, (b, c)) => ((a,b), c) }
  def productF[I,O,I2,O2](f: I => O, g: I2 => O2): (I,I2) => (O,O2) = (i,i2) => (f(i), g(i2))
}

trait Traverse[F[_]] extends Functor[F] with Foldable[F] {
  
  /** `List[A]`, `A => G[B]` => `G[List[B]]` */
  def traverse[G[_]:Applicative,A,B](fa: F[A])(f: A => G[B]): G[F[B]] =
    sequence(map(fa)(f))
    
  /** `List[G[A]]` => `G[List[A]]` */
  def sequence[G[_]:Applicative,A](fma: F[G[A]]): G[F[A]] =
    traverse(fma)(ma => ma)

  def map[A,B](fa: F[A])(f: A => B): F[B] = ???

  import Applicative._

  override def foldMap[A,B](as: F[A])(f: A => B)(mb: Monoid[B]): B =
    traverse[({type f[x] = Const[B,x]})#f,A,Nothing](
      as)(f)(monoidApplicative(mb))

  def traverseS[S,A,B](fa: F[A])(f: A => State[S, B]): State[S, F[B]] =
    traverse[({type f[x] = State[S,x]})#f,A,B](fa)(f)(Monad.stateMonad)

  def mapAccum[S,A,B](fa: F[A], s: S)(f: (A, S) => (B, S)): (F[B], S) =
    traverseS(fa)((a: A) => (for {
      s1 <- State.get[S]
      (b, s2) = f(a, s1)
      _  <- State.set(s2)
    } yield b)).run(s)

  override def toList[A](fa: F[A]): List[A] =
    mapAccum(fa, List[A]())((a, s) => ((), a :: s))._2.reverse

  def zipWithIndex[A](fa: F[A]): F[(A, Int)] =
    mapAccum(fa, 0)((a, s) => ((a, s), s + 1))._1

  def reverse[A](fa: F[A]): F[A] = ???

  override def foldLeft[A,B](fa: F[A])(z: B)(f: (B, A) => B): B = ???

  def fuse[G[_],H[_],A,B](fa: F[A])(f: A => G[B], g: A => H[B])
                         (implicit G: Applicative[G], H: Applicative[H]): (G[F[B]], H[F[B]]) = ???

  def compose[G[_]](implicit G: Traverse[G]): Traverse[({type f[x] = F[G[x]]})#f] = ???
}

object Traverse {
  val listTraverse = ???

  val optionTraverse = ???

  val treeTraverse = ???
}
