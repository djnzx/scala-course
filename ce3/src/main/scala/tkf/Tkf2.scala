package tkf

import cats.Applicative
import cats.effect.IO
import cats.implicits._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

object Tkf2 {

  def refineFuture[A](fa: Future[A])(implicit ec: ExecutionContext): Future[Either[Throwable, A]] =
    fa.map(Right(_))
      .recover(Left(_))

  def refineIO[A](fa: IO[A]): IO[Either[Throwable, A]] =
    fa.attempt

  trait Recoverable[F[_]] {
    def handle[A](fa: F[A]): F[Either[Throwable, A]]
  }

  object Recoverable {

    implicit def sepFuture(implicit ec: ExecutionContext): Recoverable[Future] = new Recoverable[Future] {
      override def handle[A](fa: Future[A]): Future[Either[Throwable, A]] =
        fa.map(Right(_)).recover(Left(_))
    }

    implicit val sepIO: Recoverable[IO] = new Recoverable[IO] {
      override def handle[A](fa: IO[A]): IO[Either[Throwable, A]] = fa.attempt
    }

  }

  def refineF[F[_]: Recoverable, A](fa: F[A]): F[Either[Throwable, A]] =
    implicitly[Recoverable[F]]
      .handle(fa)

  def clusterize[A](data: Seq[Either[Throwable, A]]): (Seq[A], Seq[Throwable]) =
    data.foldLeft((Seq.empty[A], Seq.empty[Throwable])) {
      case ((ss, ts), Left(t))  => (ss, t +: ts)
      case ((ss, ts), Right(s)) => (s +: ss, ts)
    }

  def map2[A, B, C](fa: Future[A], fb: Future[B])(f: (A, B) => C)(implicit ec: ExecutionContext): Future[C] = for {
    a <- fa
    b <- fb
  } yield f(a, b)

  def pure[A](a: A) = Future.successful(a)

  def sequencePlain[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext): Future[Seq[A]] =
    xs.foldLeft(pure(Seq.empty[A])) { (fs, f) =>
      map2(fs, f)(_ :+ _)
    }

  def repack0[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
    xs.map(refineFuture)
      .pipe(sequencePlain)
      .map(clusterize)

  def repack1[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
    xs.map(refineFuture)
      .sequence
      .map(clusterize)

  def repack2[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
    xs.traverse(refineFuture)
      .map(clusterize)

  def repack3[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
    xs.traverse(refineFuture)
      .map(_.partitionEither(_.swap))

  // 2 passes
  def repack4[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
    xs.traverse(refineFuture)
      .map(_.separate)

  // 1 pass
  def repack5[A](xs: Seq[Future[A]])(implicit ec: ExecutionContext) =
    xs.traverse(refineFuture)
      .map(_.separateFoldable)

  def repack6[A](xs: Seq[IO[A]]) =
    xs.traverse(refineIO)
      .map(_.separateFoldable)

  def repack7[F[_]: Recoverable: Applicative, A](xs: Seq[F[A]]) =
    xs.traverse(fa => refineF(fa))
      .map(_.separateFoldable)

}

class Tkf2 extends AnyFunSpec with Matchers {

  import Tkf2._
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration.DurationInt

  it("1") {

    val good = Seq("a", "b")
    val goodF = good.map(Future.successful)

    val bad = Seq(
      new IllegalArgumentException("x"),
      new IllegalArgumentException("z"),
    )
    val badF = bad.map(Future.failed)

    val data = goodF ++ badF

    val fr: Future[(Seq[Throwable], Seq[String])] = repack7(data)

    Await.result(fr, 10.seconds) match {
      case (outBad, outGood) =>
        outGood should contain allElementsOf good
        outGood should contain noElementsOf bad

        outBad should contain allElementsOf bad
        outBad should contain noElementsOf good
    }

  }
}