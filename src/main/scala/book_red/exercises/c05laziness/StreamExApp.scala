package book_red.exercises.c05laziness

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
    foldRight(AStream.empty[A]) ((a, sb) => if (p(a)) AStream.cons(a, sb) else sb)

  def map[B](f: A => B): AStream[B] = ???
  def flatMap[B](f: A => AStream[B]): AStream[B] = ???
  def append: AStream[A] = ???
  def take(n: Int): AStream[A] = ???
  def drop(n: Int): AStream[A] = ???
  def takeWhile(p: A => Boolean): AStream[A] = ???
  def forAll(p: A => Boolean): Boolean = ???

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

  val s4 = s3.filter(_ > 1)
  println(":::filtered:::")
  s4.foldRight(())((a,b) => { println(s"print:$a"); b })
}
