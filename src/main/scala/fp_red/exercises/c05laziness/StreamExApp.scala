package fp_red.exercises.c05laziness

import scala.annotation.tailrec
import scala.{Stream => _}

sealed trait AStream[+A] {
  def headOption: Option[A] = this match {
    case ACons(h, _) => Some(h())
    case AEmpty      => None
  }
  // The arrow `=>` in front of the argument type `B`
  // means that the function `f` takes its second argument
  // by name and may choose not to evaluate it.
  def foldRight[B](z: => B)(f: (A, => B) => B): B = this match {
      case ACons(h,t) => f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def exists(p: A => Boolean): Boolean = this match {
    case ACons(h, t) => if (p(h())) true else t().exists(p)
    case _           => false
  }

  def existsR(p: A => Boolean): Boolean = foldRight(false)((a,b) => p(a) || b) // b - lazy

  final def find(p: A => Boolean): Option[A] = this match {
    case AEmpty      => None
    case ACons(h, t) => if (p(h())) Some(h()) else t().find(p)
  }

  final def find2(p: A => Boolean): Option[A] = filter(p).headOption

  def filter(p: A => Boolean): AStream[A] =
    foldRight(AStream.empty[A])((a, sa) => if (p(a)) AStream.cons(a, sa) else sa)

  def map[B](f: A => B): AStream[B] =
    foldRight(AStream.empty[B])((a, sb) => AStream.cons(f(a), sb))

  def append[A1 >: A](another: AStream[A1]): AStream[A1] =
    foldRight(another)((a, sa1) => AStream.cons(a, sa1))

  def appendOne[A1 >: A](a: A1): AStream[A1] =
    append(AStream(a))

  def forAll(p: A => Boolean): Boolean =
    foldRight(true)((a, acc) => p(a) && acc)

  def flatMap[B](f: A => AStream[B]): AStream[B] =
    foldRight(AStream.empty[B])((a, sa) => f(a).append(sa))

  def take(n: Int): AStream[A] = {
    @tailrec
    def go(c: Int, acc: AStream[A], tail: AStream[A]): AStream[A] = c match {
      case 0 => acc
      case _ => tail match {
        case AEmpty => acc
        case ACons(h, t) => go(c-1, acc.appendOne(h()), t())
      }
    }
    go(n, AStream.empty, this)
  }

  def drop(n: Int): AStream[A] = {
    @tailrec
    def go(c: Int, acc: AStream[A], tail: AStream[A]): AStream[A] = c match {
      case 0 => tail match {
        case AEmpty => acc
        case ACons(h, t) => go(0, acc.appendOne(h()), t())
      }
      case _ => tail match {
        case AEmpty => AEmpty
        case ACons(_, t) => go(c-1, acc, t())
      }
    }
    go(n, AStream.empty, this)
  }

  def takeWhile(p: A => Boolean): AStream[A] = {
    @tailrec
    def go(acc: AStream[A], tail: AStream[A]): AStream[A] = tail match {
      case AEmpty      => acc
      case ACons(h, t) => if (p(h())) go(acc.appendOne(h()), t()) else acc
    }
    go(AStream.empty, this)
  }

  def dropWhile0(p: A => Boolean): AStream[A] = {
    @tailrec
    def go(skip: Boolean, acc: AStream[A], tail: AStream[A]): AStream[A] = if (skip) {
      tail match {
        case AEmpty => acc
        case ACons(h, t) => if (p(h())) go(skip, acc, t()) else go(skip = false, acc, tail)
      }
    } else {
      tail match {
        case AEmpty => acc
        case _ => append(tail)
      }
    }
    go(skip = true, AStream.empty, this)
  }

  def dropWhile(p: A => Boolean): AStream[A] = {
    @tailrec
    def go(skip: Boolean, acc: AStream[A], tail: AStream[A]): AStream[A] = tail match {
      case AEmpty      => acc
      case ACons(h, t) => if (skip) {
        if (p(h())) go(skip, acc, t()) else go(skip = false, acc, tail)
      } else append(tail)
    }
    go(skip = true, AStream.empty, this)
  }

}

case object AEmpty extends AStream[Nothing]
case class ACons[+A](h: () => A, t: () => AStream[A]) extends AStream[A]

object AStream {
  // SMART constructor to make
  def cons[A](hd: => A, tl: => AStream[A]): AStream[A] = {
    lazy val head = hd
    lazy val tail = tl
    ACons(() => head, () => tail)
  }
  def empty[A]: AStream[A] = AEmpty

  def apply[A](as: A*): AStream[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))

  val ones: AStream[Int] = AStream.cons(1, ones)

  def from(n: Int): AStream[Int] = ???

  // value producer
  def unfold[A, S](z: S)(f: S => Option[(A, S)]): AStream[A] = {
    f(z) match {
      case None        => AStream.empty
      case Some((a,s)) => AStream.cons(a, unfold(s)(f))
    }
  }

}

object StreamExApp extends App {

  val expensive = () => {
    print("expensive...")
    Thread.sleep(100)
    println("done")
    42
  }

  println("---- w.o. smart constructor")
  val s1: AStream[Int] = ACons(expensive, () => AEmpty)
  println(s1.headOption) // call `expensive`
  println(s1.headOption) // call `expensive`

  println("---- w. smart constructor")
  val s2: AStream[Int] = AStream(expensive())
  println(s2.headOption) // call `expensive`
  println(s2.headOption) // read cached value because of lazy val hd

  val s3 = AStream(1,2,3,4)
  s3.foldRight(0)((a,b) => { println(s"f:a=$a.."); a+b })

  println(s3.exists(_ == 3))
  println(s3.exists(_ == 5))
  println(s3.existsR(_ == 3))
  println(s3.existsR(_ == 5))

  val s4 = s3.filter(_ > 1).map(_ + 10).append(AStream(100,101,102))
  println(":::filtered:::")
  s4.foldRight(())((a,b) => { println(s"print:$a"); b })
  println(s4.forAll(_ > 0))
  println(s4.forAll(_ > 100))
  val s5 = s3.flatMap(x => AStream(-x, x))
  s5.foldRight(())((a,b) => { println(s"print:$a"); b })
  println(":::take(2):::")
  s5.take(2)
    .foldRight(())((a,b) => { println(s"print:$a"); b })
  println(":::drop(2):::")
  s5.drop(2)
    .foldRight(())((a,b) => { println(s"print:$a"); b })
  println(":::takeWhile:::")
  s5.takeWhile(Math.abs(_) < 3)
    .foldRight(())((a,b) => { println(s"print:$a"); b })
  println(":::dropWhile:::")
  s5.dropWhile(Math.abs(_) < 3)
    .foldRight(())((a,b) => { println(s"print:$a"); b })
  AStream.ones.take(5)
    .foldRight(())((a,b) => { println(s"print:$a"); b })
  AStream.ones.take(5)
    .map(_ + 10)
    .foldRight(())((a,b) => { println(s"print:$a"); b })
  // value producer example
  AStream.unfold(0){ (s: Int) => {
    val ns = s + 1
    val v = ns*10
    if (s<10) Some((v, ns)) else None
  }}
    .foldRight(())((a,b) => { println(s"print:$a"); b })
}
