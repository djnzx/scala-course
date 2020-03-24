package ninetynine

import scala.annotation.tailrec

object NinetyNineApp extends App {

  object P01 {
    val data: List[Int] = List(1, 1, 2, 3, 5, 8)
    @tailrec
    def last[A](as: List[A]): A = as match {
      case h::Nil => h
      case _::t   => last(t)
      case _      => throw new NoSuchElementException
    }
    def test(): Unit = {
      println(last(data)) // 8
    }
  }
//  P01.test()

  object P02 {
    val data: List[Int] = List(1, 1, 2, 3, 5, 8)

    @tailrec
    def penultimate[A](as: List[A]): A = as match {
      case h::t => t match {
        case _::Nil => h
        case _ => penultimate(t)
      }
      case _ => throw new NoSuchElementException
    }

    def test(): Unit = {
      val r = penultimate(data)
      println(r) // 5
    }
  }
//  P02.test()

  object P03 {
    val data: List[Int] = List(1, 1, 2, 3, 5, 8)
    // elements are being counted from zero
    @tailrec
    def nth[A](n: Int, as: List[A]): A = (n, as) match {
      case (0, h::_) => h
      case (n, _::t) => nth(n-1, t)
      case _         => throw new NoSuchElementException
    }

    def test(): Unit = {
      val r = nth(4, data) // 5
      println(r)
    }
  }
//  P03.test()

  object P04 {
    val data: List[Int] = List(1, 1, 2, 3, 5, 8)

    def length[A](as: List[A]): Int = {
      @tailrec
      def len(as: List[A], acc: Int): Int = as match {
        case Nil  => acc
        case _::t => len(t, acc + 1)
      }
      len(as, 0)
    }

    def test(): Unit = {
      val r = length(data) // 6
      println(r)
    }
  }
//  P04.test()

  object P05 {
    val data: List[Int] = List(1, 1, 2, 3, 5, 8)

    def reverse[A](as: List[A]): List[A] = {
      @tailrec
      def reverse(as: List[A], acc: List[A]): List[A] = as match {
        case Nil  => acc;
        case h::t => reverse(t, h :: acc)
      }
      reverse(as, Nil)
    }

    def test(): Unit = {
      println(data)
      val r = reverse(data)
      println(r)
    }
  }
//  P05.test()

  object P05a {
    def reverseAndLen[A](as: List[A]): (List[A], Int) = {
      @tailrec
      def reverse(as: List[A], acc: (List[A], Int)): (List[A], Int) = as match {
        case Nil  => acc;
        case h::t => reverse(t, (h :: acc._1, 1 + acc._2))
      }
      reverse(as, (Nil, 0))
    }
  }

  object P06 {
    val dataNo: List[Int] = List(1, 1, 2, 3, 5, 8)
    val dataY1: List[Int] = List(1, 2, 3, 3, 2, 1)
    val dataY2: List[Int] = List(1, 2, 5, 2, 1)
    val dataY3: List[Int] = List(1)
    val dataY4: List[Int] = Nil

    def isPalindrome[A](as: List[A]): Boolean = {
      import P05a.reverseAndLen
      @tailrec
      def check(n: Int, a: List[A], b: List[A]): Boolean = (n,a,b) match {
        case (0, _, _) => true
        case (_, ah::at, bh::bt) => if (ah == bh) check(n-1, at, bt) else false
      }
      val (asr, len) = reverseAndLen(as)
      check(len / 2, as, asr)
    }

    def test(): Unit = {
      println(isPalindrome(dataNo))
      println(isPalindrome(dataY1))
      println(isPalindrome(dataY2))
      println(isPalindrome(dataY3))
      println(isPalindrome(dataY4))
    }
  }
//  P06.test()

  object P07trOnlyOneLevel {
    def flatten(xsa: List[Any]): List[Any] = {
      @tailrec
      def flatten(xs: List[Any], acc: List[Any]): List[Any] = xs match {
        case Nil => acc
        case h::t => h match {
          case n: Int       => flatten(t, acc :+ n)
          case l: List[Any] => flatten(t, acc ++ l)
        }
      }
      flatten(xsa, Nil)
    }

    def test(): Unit = {
      val data: List[Any] = List(List(1, 1), 2, List(3, List(5, 8)))
      val r = flatten(data)
      println(data)
      println(r)
    }
  }
//  P07trOnlyOneLevel.test()

  object P07Untyped {
    def flatten(xs: List[Any]): List[Any] = xs match {
      case Nil => Nil
      case h::t => h match {
        case n: Int       => n :: flatten(t)
        case l: List[Any] => flatten(l) ++ flatten(t)
      }
    }

    def test(): Unit = {
      val data = List(List(1, 1), 2, List(3, List(5, List(8))))
      val r = flatten(data)
      println(data)
      println(r)
    }
  }
//  P07Untyped.test()

  object P07Typed {
    sealed trait El[+A] // A - invariant, +A - covariant, Nothing supertypw is allowed
    final case class ELI[A](a: A) extends El[A]
    final case class ELL[+A](head: A, tail: El[A]) extends El[A]
    final case object ELX extends El[Nothing]


    def test(): Unit = {
      val dataTyped1 = ELX              // EMPTY
      val dataTyped2 = ELI(1)           // ONE VALUE
      val dataTyped3 = ELL(ELI(1), ELX) // LIST OF 1 EL
      val dataTyped4 = ELL(ELI(1), ELL(ELI(2), ELX)) // LIST OF 2 EL's
    }
  }
//  P07Typed.test()

  object P08 {
    def compress(xs: List[Symbol]): List[Symbol] = {
      @tailrec
      def compress(xs: List[Symbol], prev: Symbol, acc: List[Symbol]): List[Symbol] = (xs, prev) match {
        case (Nil, _)  => acc
        case (h::t, p) => if (h == p) compress(t, p, acc) else compress(t, h, acc :+ h)
      }
      val h::t = xs
      compress(t, h, List(h))
    }

    def test(): Unit = {
      val data = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
      println(data)
      val r = compress(data)
      println(r)
    }
  }
//  P08.test()

  object P09 {

    def pack[A](xs: List[A]): List[List[A]] = {

      @tailrec
      def pack(xs: List[A], tmp: List[A], acc: List[List[A]]): List[List[A]] = (xs, tmp) match {
        case (Nil, _)  => acc :+ tmp
        case (xh::xt, th::_) => if (xh == th) pack(xt, xh::tmp, acc) else pack(xt, List(xh), acc :+ tmp)
      }

      val h::t = xs
      pack(t, List(h), Nil)
    }

    def test(): Unit = {
      val data = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
      println(data)
      val r = pack(data)
      println(r)
    }
  }
//  P09.test()

  object P10 {

    def pack[A](xs: List[A]): List[(A, Int)] = {

      @tailrec
      def pack(xs: List[A], tmp: (A, Int), acc: List[(A, Int)]): List[(A, Int)] = (xs, tmp) match {
        case (Nil, _)  => acc :+ tmp
        case (xh::xt, (ch, cnt)) =>
          if (xh == ch) pack(xt, (ch, cnt + 1), acc)        // the same letter, keep counting
          else          pack(xt, (xh, 1)      , acc :+ tmp) // the letter is different, start counting from 1
      }

      val h::t = xs
      pack(t, (h, 1), Nil)
    }

    def test(): Unit = {
      val data = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
      println(data)
      val r: List[(Symbol, Int)] = pack(data)
      println(r)
    }
  }
//  P10.test()

  object P11 {

    def pack[A](xs: List[A]) = {

      @tailrec
      def pack(xs: List[A], tmp: (A, Int), acc: List[Any]): List[Any] = (xs, tmp) match {
        case (Nil, _)  => acc :+ tmp
        case (xh::xt, (ch, cnt)) =>
          if (xh == ch) pack(xt, (ch, cnt + 1), acc)          // the same letter, keep counting
          else cnt match {
            case 1 => pack(xt, (xh, 1), acc :+ tmp._1)
            case _ => pack(xt, (xh, 1), acc :+ tmp)
          }
      }

      val h::t = xs
      pack(t, (h, 1), Nil)
    }

    def test(): Unit = {
      val data = List('x, 'a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
      println(data)
      val r = pack(data)
      println(r)
    }
  }
//  P11.test()

  object PXX {
    val data: List[Int] = List(1, 1, 2, 3, 5, 8)

    def test(): Unit = {
      val r = ???
      println(r)
    }
  }
}
