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

    sealed trait El[A]

    final case class ElItem[A](a: A) extends El[A]

    final case class ElList[A](as: El[A]*) extends El[A]

    def flatten[A](xs: List[El[A]]): List[ElItem[A]] = xs match {
      case Nil => Nil
      case h :: t => h match {
        case n: ElItem[A] => n :: flatten(t)
        case l: ElList[A] => flatten(l.as.toList) ++ flatten(t)
      }
    }

    def makeTyped(xs: List[Any]): List[El[Int]] = xs match {
      case Nil => Nil
      case h :: t => {
        if (h.isInstanceOf[Int]) {
          println(s"I:$h");
          val eli: El[Int] = ElItem[Int](h.asInstanceOf[Int])
          val elt: List[El[Int]] = makeTyped(t)
          eli :: elt
        }
        else
//        if (h.isInstanceOf[List[_]])
                { println(s"HL:$h");
                  ???
//                  ElList(h.asInstanceOf[List[_]]) :: makeTyped(t)
                }
      }
    }

    def makePlain[A](xs: List[ElItem[A]]): List[A] = {
      @tailrec
      def step(xs: List[ElItem[A]], acc: List[A]): List[A] = xs match {
        case Nil  => acc
        case h::t => step(t, h.a :: acc)
//        case _ @ ElItem(a)::t => step(t, a :: acc)
//        case _ => throw new IllegalArgumentException("Wrong type given")
      }
      step(xs, Nil) reverse
    }

    def test(): Unit = {
//      val dataUntyped = List(1, 2)
      val dataUntyped = List(List(1,2), 3)
//      val dataUntyped = List(List(1, 1), 2, List(3, List(5, List(8))))
      val dataTyped: List[El[Int]] = makeTyped(dataUntyped)
      println(dataUntyped)
      println(dataTyped)
      println("===========")
      val flattened: List[ElItem[Int]] = flatten(dataTyped)
      val plain: List[Int] = makePlain(flattened)
      println(flattened)
      println(plain)
      println("-------------")
//      val dataTyped2: List[El[Int]] = List(ElList(List(ElItem(1), ElItem(1))), ElItem(2))
//      val rtyped: List[ElItem[Int]] = flatten(dataTyped2)
//      val rplain: List[Int] = makePlain(rtyped)
//      println(dataTyped2)
//      println(rtyped)
//      println(rplain)
    }
  }
  P07Typed.test()

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

  object PXX {
    val data: List[Int] = List(1, 1, 2, 3, 5, 8)

    def test(): Unit = {
      val r = ???
      println(r)
    }
  }
}
