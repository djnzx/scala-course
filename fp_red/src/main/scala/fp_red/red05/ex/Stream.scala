package fp_red.red05.ex

import scala.{Stream => _}
import Stream._
import scala.annotation.tailrec

sealed trait Stream[+A] {

  /**
    * general, natural definition foldRight
    * from Left to Right by building function
    * from Stream(a,b,c)
    * to
    * f(a, f(b, f(c, z)))
    */
  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
    case Cons(h, t) => f( h(), t().foldRight(z)(f) )
    case Empty => z
  }

  // natural recursion
  def toList: List[A] = this match {
    case Empty => Nil
    case Cons(h, t) => h() :: t().toList
  }

  // natural recursion via foldRight
  def toList_via_fr: List[A] =
    foldRight(List.empty[A]) { (a, as) => a :: as }

  // tail recursion with accumulator
  def toList_TR: List[A] = {
    @tailrec
    def go(tail: Stream[A], acc: List[A]): List[A] = tail match {
      case Empty => acc
      case Cons(h, t) => go(t(), h() :: acc)
    }
    go(this, Nil) reverse
  }

  // mutable buffer inside
  def toList_fast: List[A] = {
    val buf = new scala.collection.mutable.ListBuffer[A]
    @tailrec
    def go(tail: Stream[A]): List[A] = tail match {
      case Empty => buf.toList
      case Cons(h, t) =>
        buf += h()
        go(t())
    }
    go(this)
  }

  // recursive, manual
  def exists_R(p: A => Boolean): Boolean = this match {
    case Cons(h, t)  => p(h()) || t().exists_R(p)
    case Empty       => false
  }

  // tail recursive, manual
  def exists_TR(p: A => Boolean): Boolean = this match {
    case Cons(h, _) if p(h()) => true
    case Cons(_, t)           => t().exists_TR(p)
    case Empty                => false
  }

  // recursive, via foldRight
  def exists_via_fr(p: A => Boolean): Boolean =
    foldRight(false) { (a, b) => if (p(a)) true else b }

  // recursive, via foldRight, pure math solution
  def exist(p: A => Boolean): Boolean =
    foldRight(false) { (a, b) => p(a) || b }

  // tail recursive, stack-friendly
  @tailrec
  final def find(p: A => Boolean): Option[A] = this match {
    case Empty => None
    case Cons(h, t) => if (p(h())) Some(h()) else t().find(p)
  }
  
  // take N elements, and drop the tail
  def take(n: Int): Stream[A] = (this, n) match {
    case (Empty,      _) => empty
    case (_,          0) => empty
    case (Cons(h, t), n) => cons(h(), t().take(n - 1))
  }

  def take_unfold(n: Int): Stream[A] =
    unfold((this, n)) { sa_n: (Stream[A], Int) => sa_n match {
      case (Empty, _)      => None
      case (_,     0)      => None
      case (Cons(h, t), n) => Some((h(), (t(), n-1)))
    }}
  
  def take_book(n: Int): Stream[A] = this match {
    case Cons(h, t) if n > 1  => cons(h(), t().take(n - 1))
    case Cons(h, _) if n == 1 => cons(h(), empty)
    case _ => empty
  }

  // drop N elements and take residuals
  @tailrec
  final def drop(n: Int): Stream[A] = (this, n) match {
    case (Empty,      _) => empty
    case (_         , 0) => this
    case (Cons(_, t), n) => t().drop(n - 1)
  }

  @tailrec
  final def drop_book(n: Int): Stream[A] = this match {
    case Cons(_, t) if n > 0 => t().drop_book(n - 1)
    case _ => this
  }

  def takeWhile(p: A => Boolean): Stream[A] = this match {
    case Cons(h, t) if p(h()) => cons(h(), t() takeWhile p)
    case _ => empty
  }

  def takeWhile_FR(p: A => Boolean): Stream[A] =
    foldRight(empty[A]) { (a, b) => 
      if (p(a)) cons(a, b) 
      else      empty
    }

  def forAll(p: A => Boolean): Boolean =
    foldRight(true) { (a, b) => p(a) && b }

  def forAll1(p: A => Boolean): Boolean = this match {
    case Empty                => true
    case Cons(h, t) if p(h()) => t() forAll p
    case _                    => false
  }

  def forAll2(p: A => Boolean): Boolean = this match {
    case Empty      => true
    case Cons(h, t) => p(h()) && t().forAll2(p)
  }

  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, _) => Some(h())
  }

  def headOption_FR: Option[A] =
    foldRight(Option.empty[A]) { (a, _) => Some(a) }

  // recursive
  def map[B](f: A => B): Stream[B] = this match {
    case Empty => Empty
    case Cons(h, t) =>
      val b: B = f(h())
      val bb: Stream[B] = t().map(f)
      cons(b, bb)
  }
  // tail recursive
  def map_TR[B](f: A => B): Stream[B] = ???
  // via foldRight
  def map_fr[B](f: A => B): Stream[B] =
    foldRight(empty[B]) { (a, b) => cons(f(a), b) }
  // via unfold
  def map_unfold[B](f: A => B): Stream[B] =
    unfold(this) { s: Stream[A] =>
      s match {
        case Empty => None 
        case Cons(h, t) => Some(f(h()), t())
      }
    }

  def filter(p: A => Boolean): Stream[A] = this match {
    case Empty => empty
    case Cons(h, t) =>
      if (p(h())) cons(h(), t().filter(p))
      else                  t().filter(p)
  }
  
  def filter_fr(p: A => Boolean): Stream[A] =
    foldRight(empty[A]) { (h, t) => 
      if (p(h)) cons(h, t)
      else      t
    }

  def append[A2 >: A](a2: Stream[A2]): Stream[A2] = this match {
    case Empty => a2
    case Cons(h, t) => cons(h(), t().append(a2))
  }
  
  def append_fr[A2 >: A](a2: Stream[A2]): Stream[A2] =
    foldRight(a2) { (h, t) => 
      cons(h, t)
    }

  // we implement recursive part manually
  def flatMap[B](f: A => Stream[B]): Stream[B] = this match {
    case Empty => empty
    case Cons(h, t) => f(h()).append(t().flatMap(f))
  }
  
  // recursive part is implemented by foldLeft
  def flatMap_fr[B](f: A => Stream[B]): Stream[B] =
    foldRight(empty[B]) { (h, t) =>
      f(h).append_fr(t)
    }
  
  def startsWith[B](s: Stream[B]): Boolean = ???
  
}

case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  // smart constructor
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  // smart constructor
  def empty[A]: Stream[A] = Empty

  // smart constructor
  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty 
    else cons(as.head, apply(as.tail: _*))

  // unlimited stream of ints 1
  val ones: Stream[Int] = Stream.cons(1, ones)
  
  def from(n: Int): Stream[Int] = cons(n, from(n+1))

  // we have an initial state (z) and function S => Opt[(A, S)] 
  def unfold[A, S](s0: S)(f: S => Option[(A, S)]): Stream[A] =
    f(s0) match {
      case Some((a, s1)) => cons(a, unfold(s1)(f))
      case None          => empty
    }
}
