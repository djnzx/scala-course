package fp_red.red04

import scala.annotation.tailrec

object EitherTrSeq {
  
  // recursive, math implementation
  def sequence[E, A](as: List[Either[E, A]]): Either[E, List[A]] = as match {
    case Nil => Right(Nil)
    case h::t => h.map2( sequence(t) )(_ :: _)
  }
  
  // tail recursion with manual accumulator handle
  def sequenceTR[E, A](as: List[Either[E, A]]): Either[E, List[A]] = {
    @tailrec
    def go(tail: List[Either[E, A]], acc: List[A]): Either[E, List[A]] = tail match {
      case Nil         => Right(acc)
      case Left(e)::_  => Left(e)
      case Right(a)::t => go(t, a :: acc)
    }
    go(as, Nil) map { _.reverse }
  }

  // recursive, math implementation
  def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = as match {
    case Nil  => Right(Nil)
    case h::t => f(h).map2( traverse(t)(f) )(_ :: _)
  }
  
  def traverseTR[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = {
    @tailrec
    def go(tail: List[A], acc: List[B]): Either[E, List[B]] = tail match {
      case Nil  => Right(acc)
      case h::t => f(h) match {
        case Right(b)   => go(t, b :: acc)
        case l: Left[E] => l
      }
    }
    go(as, Nil) map { _.reverse }
  } 

  def sequence_via_traverse[E, A](as: List[Either[E, A]]): Either[E, List[A]] =
    traverse(as)(identity)
  
  def traverse_via_sequence[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] = {
    val leb: List[Either[E, B]] = as.map(f)
    sequence(leb)
  }

}
