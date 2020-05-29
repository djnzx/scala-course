package scalaz

import scala.language.implicitConversions
import Scalaz._
object Day01App1Polymorphism extends App {

  // base idea
  trait Plus1[A] {
    def plus(a: A): A
  }

  def plus1[A <: Plus1[A]](a1: A, a2: A): A = a1.plus(a2)

  // far more better
  trait Plus[A] {
    def plus(a1: A, a2: A): A
  }

  def plus0[A](a1: A, a2: A)(implicit ev: Plus[A]): A = ev.plus(a1, a2)
  def plus[A: Plus](a1: A, a2: A): A = implicitly[Plus[A]].plus(a1, a2)

  implicit val plus_int: Plus[Int] = new Plus[Int] {
    override def plus(a1: Int, a2: Int): Int = a1 + a2
  }

  implicit val plus_str: Plus[String] = new Plus[String] {
    override def plus(a1: String, a2: String): String = s"$a1$a2"
  }

  val r1: Int = plus(1,1)
  val r2: String = plus("Hello ", "World")

  /** Monoid
    * f: (A,A) => A
    * A0: f(A,A0) => A
    * A0: f(A0,A) => A
    */
  val sx: (Int, Int) = List(1,2,3).foldLeft((0,0))((t, a) => (t._1+1,t._2+a))

  println(s"sx = ${sx}")
  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }
  object Monoid {
    implicit val IntMonoid: Monoid[Int] = new Monoid[Int] {
      def mappend(a: Int, b: Int): Int = a + b
      def mzero: Int = 0
    }
    implicit val StringMonoid: Monoid[String] = new Monoid[String] {
      def mappend(a: String, b: String): String = a + b
      def mzero: String = ""
    }
  }
  def sum[A: Monoid](xs: List[A]): A = {
    val m = implicitly[Monoid[A]]
    xs.foldLeft(m.mzero)(m.mappend)
  }

  val multiMonoid: Monoid[Int] = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero: Int = 1
  }

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B): B
  }
  object FoldLeft {
    implicit val FoldLeftList: FoldLeft[List] = new FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B): B = xs.foldLeft(b)(f)
    }
  }

  def sum[M[_]: FoldLeft, A: Monoid](xs: M[A]): A = {
    val m = implicitly[Monoid[A]]
    val fl = implicitly[FoldLeft[M]]
    fl.foldLeft(xs, m.mzero, m.mappend)
  }

  sum(List(1, 2, 3, 4))
  sum(List("a", "b", "c"))

  trait MonoidOp[A] {
    val F: Monoid[A]
    val value: A
    def |+|(a2: A): A = F.mappend(value, a2)
  }

  implicit def toMonoidOp[A: Monoid](a: A): MonoidOp[A] = new MonoidOp[A] {
    override val F: Monoid[A] = implicitly[Monoid[A]]
    override val value: A = a
  }

//  val z1 = 1 |+| 2     // 3
//  val z2 = "A" |+| "B" // "AB"
//
//  1.some === "Person"
}
