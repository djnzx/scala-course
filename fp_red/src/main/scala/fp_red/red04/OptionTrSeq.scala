package fp_red.red04

import scala.annotation.tailrec

object OptionTrSeq {
  
  def map2[A,B,C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = for {
    a <- fa
    b <- fb
    c = f(a, b)
  } yield c

  def map2b[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a.flatMap(av => b.map(bv => f(av, bv)))

  // tail recursive implementation, implies manual accumulator handling
  def sequenceTR[A](oas: List[Option[A]]): Option[List[A]] = {
    @tailrec
    def go(oast: List[Option[A]], acc: List[A]): Option[List[A]] = oast match {
      case Nil => Some(acc)
      case h::t => h match {
        case None => None
        case Some(a) => go(t, a::acc)
      }
    }
    go(oas, Nil) map { _ reverse }
  }

  // recursive implementation with automatic (in stack) accumulation
  def sequenceR1[A](oas: List[Option[A]]): Option[List[A]] = oas match {
    case Nil  => Some(Nil)
    case h::t => map2(h, sequenceR1(t)) { (x: A, xs: List[A] ) => x :: xs }
  }

  def sequenceR2[A](oas: List[Option[A]]): Option[List[A]] = oas match {
    case Nil  => Some(Nil)
    case h::t => map2(h, sequenceR2(t)) { (x, xs) => x :: xs }
  }

  def sequence[A](oas: List[Option[A]]): Option[List[A]] = oas match {
    case Nil  => Some(Nil)
    case h::t => map2(h, sequence(t))(_ :: _)
  }

  // tail recursive implementation
  def traverseTR[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = {
    @tailrec
    def go(ast: List[A], acc: List[B]): Option[List[B]] = ast match {
      case Nil  => Some(acc)
      case h::t => f(h) match {
        case Some(b) => go(t, b :: acc)
        case _       => None
      }
    }
    go(as, Nil) map { _ reverse }
  }

  def traverseR1[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = as match {
    case Nil  => Some(Nil)
    case h::t => map2(f(h), traverseR1(t)(f)) { (x: B, xs: List[B]) => x :: xs }
  }

  // recursive implementation with automatic (in stack) accumulation
  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = as match {
    case Nil => Some(Nil)
    case h::t => map2(f(h), traverse(t)(f)) { _ :: _ }
  }
  
  def sequence_via_traverse[A](oas: List[Option[A]]): Option[List[A]] =
    traverse(oas)(identity)
  
  def traverse_via_sequence[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] =
    sequence(as.map(f))

}
