package tinkoff

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import cats.implicits._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.chaining.scalaUtilChainingOps

/** На вход Seq[Future[String]] Получить Future[(Seq[String], Seq[Throwable]) - результат агрегации выполненых Future и
  * исключений
  */
object Tinkoff2 extends App {

  object common {

    /** make Throwable explicit */
    def refine[A](fa: Future[A])(implicit ec: ExecutionContext): Future[Either[Throwable, A]] =
      fa.map(Right(_)).recover(Left(_))

    /** repack results */
    def repack[A](data: Seq[Either[Throwable, A]]): (Seq[A], Seq[Throwable]) =
      data.foldLeft((Seq.empty[A], Seq.empty[Throwable])) {
        case ((ss, ts), Left(t))  => (ss, t +: ts)
        case ((ss, ts), Right(s)) => (s +: ss, ts)
      }

  }

  object ImplementationCats {
    import common._

    def sequenceFutureCats[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
      xs.map(refine)
        .sequence
        .map(repack)

    def sequenceCats[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
      xs.traverse(refine)
        .map(repack)

  }

  object ImplementationPlain {

    import common._

    def map2[A, B, C](fa: Future[A], fb: Future[B])(f: (A, B) => C)(implicit ec: ExecutionContext): Future[C] = for {
      a <- fa
      b <- fb
    } yield f(a, b)

    def sequencePlain[A](
        xs: Seq[Future[A]],
      )(
        implicit ec: ExecutionContext,
      ): Future[Seq[A]] =
      xs.foldLeft(Future.successful(Seq.empty[A])) { (fs, f) =>
        map2(fs, f)(_ :+ _)
      }

    def sequenceCombined[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext): Future[(Seq[A], Seq[Throwable])] =
      xs.map(refine)
        .pipe(sequencePlain)
        .map(repack)

  }

  def impl[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
//    ImplementationCats.sequenceCats[A](xs)
    ImplementationPlain.sequenceCombined[A](xs)
}

class Tinkoff2Spec extends AnyFunSpec with Matchers {

  import Tinkoff2.{impl => sequence}
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
