package cats101.c113writer

import cats.data.Writer
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxWriterId}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object C113Exercise1 extends App {

  def slowly[A](a: => A): A =
    try a finally Thread.sleep(100)

  def fact(n: Int): Int = {
    val a = slowly { if (n == 1 ) 1 else n * fact(n - 1) }
    println(s"calculated: fact($n): $a")
    a
  }

  Future { fact(10) }
  Future { fact(10) }

  Thread.sleep(5.seconds.toMillis)
}

object C113Exercise1Solution extends App {

  type Log = List[String]
  type Logged[A] = Writer[Log, A]

  def slowly[A](a: => A): A = try a finally Thread.sleep(100)

  def logMsg(n: Int, a: Int) = s"calculated in thread: ${Thread.currentThread().getName}: fact($n): $a\n"
  def logMsgLifted(n: Int, a: Int) = List(logMsg(n, a)).tell

  /** lifts to Logged[Unit] for further flatMap */
  val w0a: Logged[Unit] = List("message").tell
  /** lifts to Logged[Int] for further map/flatMap */
  val w0b: Logged[Int] = 1.pure[Logged]

  /** version with scala syntactic sugar */
  def fact0(n: Int): Logged[Int] = for {
    a <- if (n == 0) {
           /** just construct the pure value */
           val q: Logged[Int] = 1.pure[Logged]
           q
         }
         else slowly {
           /** just map the monad to have new value */
           val w: Logged[Int] = fact0(n - 1).map(_ * n)
           w
         }
        /** no matter where you are, add log line */
    _   <- List(logMsg(n, a)).tell
  } yield a

  def fact1(n: Int): Logged[Int] = {
    /** put the next value to the context */
    val w = if (n == 0) 1.pure[Logged]
            else slowly { fact1(n - 1).map(_ * n) }

    /** access the value via flatMan */
    w.flatMap { a =>
      /** lift log message to the context */
      logMsgLifted(n, a)
        .map(_ => a)
    }
  }

  def fact2(n: Int): Logged[Int] = {
    val w = if (n == 0) 1.pure[Logged]
            else slowly { fact2(n - 1).map(_ * n) }

    val w2 = w.mapBoth { (log, a) => (log :+ logMsg(n, a), a) }
    w2
  }

  def fact3tr(n: Int, w: Logged[Int] = 1.pure[Logged]): Logged[Int] =
  if (n == 0) w
  else slowly { fact3tr(n - 1, w.mapBoth { (log, a) => (log :+ logMsg(n, a), n * a) }) }

  val r1 = Future { fact3tr(10).run }
  val r2 = Future { fact3tr(10).run }

  val r = Await.result(Future.sequence(List(r1, r2)), 5.seconds)
  println(r)
}

