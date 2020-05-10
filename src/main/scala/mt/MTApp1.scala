package mt

import cats.Monad
import cats.data.EitherT
import cats.syntax.OptionOps

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object MTApp1 extends App {

  type IOResult[+A] = Either[Exception, Option[A]]
  // unit, apply:
  val l1: List[Int] = List(1)
  // map:
  val l2: List[Int] = List(1,2,3) map { _ * 2}
  // flatMap:
  val l3: List[Int] = List(1,2,3) flatMap { x => List(x, x+100) }

  // Monad - put extra capabilities
  val user: Option[Int] = Some(10)
  val data: List[Int] = List(1,2,3)
  // we want to use them in one flatMap / for comprehension

  val r: List[Int] = for {
    a <- List(1,2,3)
    b <- List(10)
    x <- Option(1)
    y <- Option.empty[Int]
  } yield a * b + x + y

  //println(r)

  val customGreeting:  Future[Option[String]] = Future.successful(Some("welcome back, Lola"))

  val excitedGreeting: Future[Option[String]] = customGreeting.map(_.map(_ + "!"))
  val hasWelcome:      Future[Option[String]] = customGreeting.map(_.filter(_.contains("welcome")))
  val noWelcome:       Future[Option[String]] = customGreeting.map(_.filterNot(_.contains("welcome")))
  val withFallback:    Future[String]         = customGreeting.map(_.getOrElse("hello, there!"))

  import cats.data.OptionT
  import cats.implicits._

  val customGreetingT:    OptionT[Future, String] = OptionT(customGreeting)

  val ot_excitedGreeting: OptionT[Future, String] = customGreetingT.map(_ + "!")
  val ot_withWelcome:     OptionT[Future, String] = customGreetingT.filter(_.contains("welcome"))
  val ot_noWelcome:       OptionT[Future, String] = customGreetingT.filterNot(_.contains("welcome"))
  val ot_withFallback:    Future[String]          = customGreetingT.getOrElse("hello, there!")

  val lo: List[Option[Int]] = List(Some(1), None, Some(2), None, Some(3))
  val lot: OptionT[List, Int] = OptionT(lo)
  val lx: OptionT[List, Int] = lot.filter(_ > 0)
  val a0: Option[Int] = lx.get(0)

  val o1f: Future[Option[Int]] = Future { Option(1) }
  val o2: OptionT[Future, Int] = OptionT(o1f).map(_*10)
  val o3: Future[Int] = o2.getOrElse(-999)
  println(o3)

  val r1: Option[Right[Nothing, Int]] = Option(Right(5))
  val r2: EitherT[Option, Nothing, Int] = EitherT(r1)
  val r3: EitherT[Option, Nothing, Int] = r2.map(_*10) // 50
  val r4: Option[Int] = r3.getOrElse(-99)

  val ooi = Some(Some(20))
  val oot: OptionT[Option, Int] = OptionT(ooi)
  val oot2: OptionT[Option, Int] = oot.map { _ / 10}
  val oot3: Option[Int] = oot2.getOrElse(-9)

  def convert[F[_], A, B](fa: F[A])(f: A => B): F[B] = ???

}
