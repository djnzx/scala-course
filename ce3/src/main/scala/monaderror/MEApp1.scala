package monaderror

import cats._
import cats.data._
import cats.effect._
import cats.implicits._

/** [[Apply]]                [[Apply]]           [[Apply]]
  *       \                       |                   |
  * [[Applicative]][F]       [[FlatMap]][F] with [[Applicative]][F]
  *         \                        \      /
  * [[ApplicativeError]][F, E] with [[Monad]][F]
  *            \          /
  *          [[MonadError]][F, E]
  *                |
  *          [[MonadCancel]][F, Throwable] with [[Defer]][F]
  *                      \             /
  *                         [[Sync]][F]
  */
class MonadErrorStudy[F[_]: Sync] {

  val ME = MonadError[F, Throwable]
  val ISX = new IllegalStateException()
  val IAX = new IllegalArgumentException()
  val count = 1

  /** throw */
  val m01: F[Int] = ME.raiseError[Int](ISX)
  val m02: F[Unit] = ME.raiseWhen(count > 3)(ISX)
  val m03: F[Unit] = ME.raiseUnless(count > 0)(ISX)

  /** handle simple (full) */
  val m04: F[Int] = m01.handleError(t => -1)

  /** handle simple (full) Monadic */
  val m05: F[Int] = m01.handleErrorWith(t => (-2).pure[F])

  /** => Either */
  val m06: F[Either[Throwable, Int]] = m01.attempt
  val m07: EitherT[F, Throwable, Int] = m01.attemptT

  /** recover (partial) */
  val m08: F[Int] = m01.recover {
    case _: IllegalArgumentException => -1
    case _: IllegalStateException    => -2
  }

  /** recover (partial) Monadic */
  val m09: F[Int] = m01.recoverWith {
    case _: IllegalArgumentException => (-11).pure[F]
    case _: IllegalStateException    => (-22).pure[F]
  }

  /** pattern match only specific type => Either, everything else allow to throw */
  val m10: F[Either[IllegalArgumentException, Int]] = m01.attemptNarrow[IllegalArgumentException]

  /** keep type, but change the value */
  val m11: F[Int] = m01.adaptError { case x => x }

  /** apply both functions */
  val m12: F[Option[Int]] = m01.redeem(t => None, _.some)

  /** apply both functions, Monadic */
  val m13: F[Option[Int]] = m01.redeemWith(t => none[Int].pure[F], _.some.pure[F])

  /** run callback on error, and rethrow */
  val m14: F[Int] = m01.onError { case x: IllegalStateException =>
    Sync[F].delay(println(x))
  }

  /** if throwable => provide a value */
  val m15: F[Int] = m01.orElse(33.pure[F])

  /** if throwable => provide another throwable */
  val m16: F[Int] = m01.orRaise(new Exception())

  /** apply predicate, or throwable */
  val m17: F[Int] = m01.ensure(IAX)(_ > 0)

  /** apply predicate, or a -> throwable */
  val m18: F[Int] = m01.ensureOr(i => IAX)(_ > 0)

  /** from Option */
  val oa: Option[Int] = ???
  val m20: F[Int] = ME.fromOption(oa, IAX)

  /** from Either */
  val ea: Either[Throwable, Int] = ???
  val m21: F[Int] = ME.fromEither(ea)

  /** from Validated, Try... */
  val fe: F[Either[Throwable, Int]] = ???

  /** attempt - inverse */
  val m22: F[Int] = fe.rethrow

  val fa: F[Option[Int]] = ???

  def make(x: Int): F[String] = x match {
    case 1 => ME.raiseError(IAX)
    case 2 => ME.raiseError(ISX)
    case n => n.toString.pure[F]
  }
}

object MEApp1 extends IOApp.Simple {

  val MES = new MonadErrorStudy[IO]

  override def run: IO[Unit] = for {
    s <- MES
           .make(1)
           .attemptNarrow[IllegalArgumentException]
    _ <- IO.println(s)
  } yield ()

}
