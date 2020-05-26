package fp_red.red01_05

object Red02 extends App {

  sealed trait Tree[+A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  sealed trait Maybe[+A] {
    def map[B](f: A => B): Maybe[B]
    def flatMap[B](f: A => Maybe[B]): Maybe[B]
    def getOrElse[B >: A](default: => B): B
    def orElse[B >: A](value: Option[B]): Option[B]
    def filter(f: A => Boolean): Maybe[A]
  }

//  case class Some[A](value: A) extends Maybe[A]
//  case object None extends Maybe[Nothing]

  def lift[A, B](f: A => B): Option[A] => Option[B] = oa => oa map f

  val inc: Int => Int = (a: Int) => a + 1

  val inco: Option[Int] => Option[Int] = lift(inc)

  sealed trait Or[+E, +A]
  case class Left[+E, Nothing](value: E) extends Or[E, Nothing]
  case class Right[Nothing, +A](value: A) extends Or[Nothing, A]


}
