package catsx

import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

import scala.annotation.tailrec

object C113WriterExercise3 extends App {

  type Logged[A] = Writer[Vector[String], A]

  // idiomatic implementation
  def factr1(n: Int): Logged[Int] = for {
    r <- if (n==1) 1.pure[Logged] else factr1(n-1).map(_ * n)
    _ <- Vector(s"$n! = $r").tell
  } yield r

  // my naive implementation
  def factr2(n: Int): Logged[Int] = {
    if (n==1) Writer(Vector("1! = 1"), 1)
    else factr2(n-1).mapBoth((log, value) => {
      val r = value * n
      (log.appended(s"$n! = $r"), r)
    })
  }

  // tail recursive runner
  def facttr(n: Int): Logged[Int] = facttr(n, 1, Writer(Vector("1! = 1"), 1))

  // tail recursive implementation
  @tailrec
  def facttr(n: Int, cur: Int, acc: Logged[Int]): Logged[Int] =
    if (cur == n) acc
    else facttr(n, cur + 1,
      for {
        r <- acc.map(_ * (cur + 1))
        _ <- Vector(s"${cur+1}! = $r").tell
      } yield r
    )

  val r1: Logged[Int] = factr1(5)
  println(r1)

  val r2: Logged[Int] = factr2(5)
  println(r2)

  val r3: Logged[Int] = facttr(5)
  println(r3)
}
