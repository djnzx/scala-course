package interview.general

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.{Option => _}
import scala.{Some => _}
import scala.{None => _}

object Q6_OptionFromScratch {

  sealed trait Option[+A] {

    /** basic */
    def flatMap[B](f: A => Option[B]): Option[B] = this match {
      case Some(a) => f(a)
      case _       => None
    }
    def map[B](f: A => B): Option[B] = flatMap(a => Some(f(a)))
    def filter(p: A => Boolean): Option[A] = flatMap(a => if (p(a)) Some(a) else None)

    /** advanced */
    def flatten[AA](implicit ev: A <:< Option[AA]): Option[AA] = flatMap(identity[A])
    def fold[B](ifNone: => B)(f: A => B): B = this match {
      case Some(a) => f(a)
      case _       => ifNone
    }
    def getOrElse[AA >: A](other: => AA): AA = fold(other)(identity)
  }

  case class Some[A](a: A) extends Option[A]
  case object None extends Option[Nothing]

  object Option {
    def some[A](a: A): Option[A] = Some(a)
    def none[A]: Option[A] = None
    def apply[A](a: A): Option[A] = if (a == null) None else Some(a)
  }
}

class Q6_OptionFromScratchSpec extends AnyFunSpec with Matchers {

  import Q6_OptionFromScratch._

  describe("syntax") {

    it("creating") {
      val o1: Option[Int] = Option.some(5)
      val o2: Option[String] = Option.some("abc")
      val n: Option[Int] = Option.none[Int]
    }

  }

  describe("operations") {

    it("basic") {
      Option(1).map(_ + 10) shouldEqual Option(11)
      Option(1).map(_ + 10).map(_.toString) shouldEqual Option("11")
      Option(10).flatMap(x => Some(x + 10)) shouldEqual Some(20)
    }

    it("intermediate") {
      Option(10).filter(_ > 20) shouldEqual None
    }

    it("advanced") {
      Option(Option(10)).flatten shouldEqual Some(10)
      Option.none[Nothing].flatten shouldEqual None

      Option(22).getOrElse(33) shouldEqual 22
      None.getOrElse(33) shouldEqual 33

      Option(33).fold("*")(_.toString) shouldEqual "33"
      None.fold("*")(_.toString) shouldEqual "*"
    }

  }
}
