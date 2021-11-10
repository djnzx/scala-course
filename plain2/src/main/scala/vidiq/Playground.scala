package vidiq

/** Cinema Tickets
  *
  * Imagine that you need to create a service for selling cinema tickets. This service should have only two features:
  *
  *   - Reserve seat(-s) — imitates a buy ticket function
  *   - Release seat(-s) — imitates order cancellation
  *
  * For sake of simplicity, let's put that the cinema hall dimension is 5x8 (5 rows, 8 seats in each row) Send a message
  * to everyone
  */
object Playground extends App {

  implicit class PureSyntax[A](private val a: A) extends AnyVal {
    def pure[F[_]]: F[A] = ???
  }

  implicit class CoerceSyntax[L, R](private val e: Either[L, R]) extends AnyVal {
    def coerceL[L2](implicit ev: L2 <:< L) = e.asInstanceOf[Either[L2, R]]
    def coerceR[R2](implicit ev: R2 <:< R) = e.asInstanceOf[Either[L, R2]]
  }

  case class Rq(row: Int, seat: Int)

  sealed trait ReserveErr
  object ReserveErr {
    case class WrongPosition(row: Int, seat: Int) extends ReserveErr
    case class AlreadyOccupied() extends ReserveErr
  }

  trait ReleaseErr
  object ReleaseErr {
    case class WrongPosition(row: Int, seat: Int) extends ReleaseErr
  }

  trait CinemaApi[F[_]] {
    def reserve(rq: Rq): F[Either[ReserveErr, Unit]]
    def release(rq: Rq): F[Either[ReleaseErr, Unit]]
  }

  sealed trait SeatState
  object Free extends SeatState
  object Occupied extends SeatState

  class CinemaInMemory[F[+_]](nRows: Int, nCols: Int) extends CinemaApi[F] {

    private val data = Array.fill(nRows)(Array.fill[SeatState](nCols)(Free))

    private def isValid(row: Int, seat: Int): Boolean =
      row >= 0 && seat >= 0 && row < data.length && seat < data(0).length

    private def isFree(row: Int, seat: Int): Boolean =
      data(row)(seat) == Free

    private def reserveAt(row: Int, seat: Int) =
      data(row)(seat) = Occupied

    private def releaseAt(row: Int, seat: Int) =
      data(row)(seat) = Free

    override def reserve(rq: Rq): F[Either[ReserveErr, Unit]] = isValid(rq.row, rq.seat) match {
      case true if isFree(rq.row, rq.seat) =>
        reserveAt(rq.row, rq.seat)
        Right(())
          .coerceL[ReserveErr]
          .pure[F]
      case true =>
        Left(ReserveErr.AlreadyOccupied())
          .coerceR[Unit]
          .pure[F]
      case _ =>
        Left(ReserveErr.WrongPosition(rq.row, rq.seat))
          .coerceR[Unit]
          .pure[F]
    }

    override def release(rq: Rq): F[Either[ReleaseErr, Unit]] = isValid(rq.row, rq.seat) match {
      case true =>
        releaseAt(rq.row, rq.seat)
        Right(())
          .coerceL[ReleaseErr]
          .pure[F]
      case _ =>
        Left(ReleaseErr.WrongPosition(rq.row, rq.seat))
          .coerceR[Unit]
          .pure[F]
    }
  }

}
