package tinkoff

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import cats.implicits._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/** На вход Seq[Future[String]] Получить Future[(Seq[String], Seq[Throwable]) - результат агрегации выполненых Future и
  * исключений
  */
object Tinkoff2 extends App {

  def repack(data: Seq[Either[Throwable, String]]): (Seq[String], Seq[Throwable]) =
    data.foldLeft((Seq.empty[String], Seq.empty[Throwable])) {
      case ((ss, ts), Left(t))  => (ss, t +: ts)
      case ((ss, ts), Right(s)) => (s +: ss, ts)
    }

  /** the problem is, that
    * {{{
    *   Future[A]
    * }}}
    * is actually
    * {{{
    *   Future[Either[Throwable, A]]
    * }}}
    */
  def refine[A](fa: Future[A])(implicit ec: ExecutionContext): Future[Either[Throwable, A]] =
    fa.map(Right(_)).recover(Left(_))

  def sequence(xs: Seq[Future[String]])(implicit ec: ExecutionContext): Future[(Seq[String], Seq[Throwable])] =
    xs.traverse(refine).map(repack)

}

class Tinkoff2Spec extends AnyFunSpec with Matchers {

  import Tinkoff2.sequence
  import scala.concurrent.duration.DurationInt
  import scala.concurrent.ExecutionContext.Implicits.global

  it("1") {

    val good = Seq("a", "b")
    val goodF = good.map(Future.successful)

    val bad = Seq(
      new IllegalArgumentException("x"),
      new IllegalArgumentException("z"),
    )
    val badF = bad.map(Future.failed)

    val data = goodF ++ badF

    val fr: Future[(Seq[String], Seq[Throwable])] = sequence(data)
    val (outGood, outBad): (Seq[String], Seq[Throwable]) = Await.result(fr, 10.seconds)

    outGood should contain allElementsOf good
    outGood should contain noElementsOf bad

    outBad should contain allElementsOf bad
    outBad should contain noElementsOf good

  }

}
