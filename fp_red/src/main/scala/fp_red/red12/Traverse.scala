package fp_red.red12

import fp_red.red06.State
import fp_red.red10.{Foldable, Monoid}
import fp_red.red11.Functor

/**
  * traversable functor
  * traverse and sequence don't depend on flatMap
  *
  * `Foldable` can't extend `Functor`
  */
trait Traverse[F[_]] extends Functor[F] with Foldable[F] { self =>

  /**
    * `List[A]`, `A => G[B]` => `G[List[B]]`
    */
  def traverse[G[_]:Applicative,A,B](fa: F[A])(f: A => G[B]): G[F[B]] =
    sequence(map(fa)(f))

  /**
    * `List[G[A]]` => `G[List[A]]`
    *
    * if `G[_]` is `Applicative` - we can change order
    *
    * `List[Option[A]] => Option[List[A]]`
    *
    * `Tree[Option[A]] => Option[Tree[A]]`
    *
    * `Map[K, Par[A]] => Par[Map[K,A]]`
    *
    */
  def sequence[G[_]:Applicative,A](fma: F[G[A]]): G[F[A]] =
    traverse(fma)(ma => ma)

  def map[A,B](fa: F[A])(f: A => B): F[B] = ???

  import Applicative._

  /** `Foldable.foldMap` via `traverse` */
  override def foldMap[A,B](as: F[A])(f: A => B)(mb: Monoid[B]): B =
    traverse[({type f[x] = Const[B, x]})#f, A, Nothing](
      as)(f)(monoidApplicative(mb))

  /** `TraverseState` via `traverse`:
    * 
    * `(F[A], A => State[S, B]) => State[S, F[B]]`
    */
  def traverseS[S,A,B](fa: F[A])(f: A => State[S, B]): State[S, F[B]] =
    traverse[({type f[x] = State[S, x]})#f, A, B](fa)(f)(Monad.stateMonad)

  /** `zipWithIndex` via `traverseS` */
  def zipWithIndex_traverseS[A](ta: F[A]): F[(A, Int)] =
    traverseS(ta) { a: A =>
      //            State, Value
      val st: State[Int, (A, Int)] = for {
        i <- State.get[Int]
        i2 = i + 1
        _ <- State.set(i2)
      } yield (a, i)
      
      st
    }
      .run(0) // (A, Int) - run by providing initial state (index 0)
      ._1         // A
  
  /** `toList` via `traverseS` */
  def toList_traverseS[A](fa: F[A]): List[A] =
    traverseS(fa) { a: A =>
      //            State, Value
      val st: State[List[A], Unit] = for {
        as <- State.get[List[A]]
        as2 = a :: as
        _ <- State.set(as2) 
      } yield () // collect everything in state and don't care about the value
    
      st
    }
      .run(List.empty)
      ._2
      .reverse
  
  /** generalising State operations */
  def mapAccum[S,A,B](fa: F[A], s0: S)(f: (A, S) => (B, S)): (F[B], S) =
    traverseS(fa) { a: A =>
      
      val st: State[S, B] = for {
        s1 <- State.get[S] // function s => (s, s)
        (b, s2) = f(a, s1)
        _ <- State.set(s2)
      } yield b
      
      st
    }.run(s0)

  /** `toList` via `mapAccum + (a, s) => ((), a :: s)` */
  override def toList[A](fa: F[A]): List[A] = 
    mapAccum(fa, List.empty[A])(
      (a, s) => ((), a :: s)
    )              // (F[Unit], List[A]) 
      ._2          // List[A]
      .reverse     // List[A]
  
  /** `zipWithIndex` via `mapAccum + (a, s) => ((a, s), s + 1)` */
  def zipWithIndex[A](fa: F[A]): F[(A, Int)] =
    mapAccum(fa, 0) { 
      (a, s) => ((a, s), s + 1)
    }
      ._1
      
  /** zip and flatten same lengths */
  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    mapAccum(fa, toList(fb) /* initial state */) {
      case (_, Nil)   => sys.error("zip: incompatible shapes")
      case (a, b::bs) => ((a, b), bs) 
    } // (F[(A, B)], List[B])
      ._1

  /** zip and flatten different lengths */
  def zipL[A,B](fa: F[A], fb: F[B]): F[(A, Option[B])] =
    mapAccum(fa, toList(fb)) {
      case (a, Nil)   => ((a, None), Nil)
      case (a, b::bs) => ((a, Some(b)), bs)
    }._1

  /** zip and flatten different lengths */
  def zipR[A,B](fa: F[A], fb: F[B]): F[(Option[A], B)] =
    mapAccum(fb, toList(fa)) {
      case (b, Nil)   => ((None,    b), Nil)
      case (b, a::as) => ((Some(a), b), as)
    }._1
  
  def reverse[A](fa: F[A]): F[A] = {
    val lar: List[A] = toList(fa).reverse
    mapAccum(fa, lar) { (_, as) => 
      (as.head, as.tail)
    }     // (F[A], List[A])
      ._1 // F[A]
  }

  override def foldLeft[A,B](fa: F[A])(z: B)(f: (B, A) => B): B =
    mapAccum(fa, z) { (a, b) => 
      ((), f(b, a))
    } // (Unit, B)
      ._2
    
  def fuse[G[_], H[_], A, B](fa: F[A])(
    f: A => G[B], 
    g: A => H[B])(implicit G: Applicative[G], H: Applicative[H]): 
  (G[F[B]], H[F[B]]) =
    traverse[({ type f[x] = (G[x], H[x]) })#f, A, B](fa) { a => 
      (f(a), g(a))
    } (G product H)

  def compose[G[_]](implicit G: Traverse[G]): Traverse[({type f[x] = F[G[x]]})#f] =
    new Traverse[({type f[x] = F[G[x]]})#f] {
      override def traverse[M[_]: Applicative,A,B](fa: F[G[A]])(f: A => M[B]): M[F[G[B]]] =
        self.traverse(fa) { ga: G[A] => 
          G.traverse(ga)(f)
        }
    }
    
}

object Traverse {

  val listTraverse: Traverse[List] = new Traverse[List] {
    override def traverse[G[_], A, B](fa: List[A])(f: A => G[B])(implicit G: Applicative[G]): G[List[B]] =
      fa.foldRight(G.unit(List.empty[B])) { (a: A, glb: G[List[B]]) =>
        G.map2(f(a), glb) { case (b, lb) => b :: lb }
      }
  }

  val optionTraverse: Traverse[Option] = new Traverse[Option] {
    override def traverse[G[_], A, B](fa: Option[A])(f: A => G[B])(implicit G: Applicative[G]): G[Option[B]] =
      fa match {
        case Some(a) => G.map(f(a))(Some(_))
        case None    => G.unit(None)
      }
  }

  val treeTraverse: Traverse[Tree] = new Traverse[Tree] {
    override def traverse[G[_], A, B](fa: Tree[A])(f: A => G[B])(implicit G: Applicative[G]): G[Tree[B]] =
      G.map2(
        f(fa.head),
        listTraverse.traverse(fa.tail)(traverse(_)(f))
      )(Tree(_, _))
  }
  
}
