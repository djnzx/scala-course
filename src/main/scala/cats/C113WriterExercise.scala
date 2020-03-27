package cats

import cats.data.{Writer, WriterT}
import cats.syntax.writer._
import cats.syntax.applicative._
import cats.instances.vector._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object C113WriterExercise extends App {
  type Logged[A] = Writer[Vector[String], A]

  // just make everything slowly
  def slowly[A](body: => A): A =
    try body finally Thread.sleep(200)

  def myLift1(n: Int): Writer[Vector[String], Int] =
    Writer[Vector[String], Int](implicitly[Monoid[Vector[String]]].empty, n)

  val vm = implicitly[Monoid[Vector[String]]]

  def myLift2(n: Int): WriterT[Id, Vector[String], Int] =
    Writer[Vector[String], Int](vm.empty, n)

  def fact0(n: Int): Int =
    if (n==0) 1 else fact0(n-1) * n

  def fact1(n: Int): Logged[Int] =
    for {
      r <- if (n == 0) 1.pure[Logged]
           else slowly(fact1(n - 1).map(_ * n))
      _ <- Vector(s"fact $n = $r").tell
    } yield r

  def fact2(n: Int): Writer[Vector[String], Int] = {
    val w1 = if (n == 0) 1.pure[Logged] else slowly(fact2(n - 1).map(_ * n))
    val w2 = w1.flatMap(r => Vector(s"fact $n = $r").tell.map(_ => r))
    w2
  }

  val res: Vector[(Vector[String], Int)] = Await.result(Future.sequence(Vector(
    Future { fact1(5).run },
    Future { fact2(6).run },
  )), 10 second)
  res.foreach(t => {
    println(s"Log:\n${t._1.mkString("\n")}")
    println(s"Value:${t._2}")
  })
}
