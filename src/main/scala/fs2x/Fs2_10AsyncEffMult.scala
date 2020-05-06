package fs2x

import fs2._
import fs2.concurrent._
import cats.effect.{ConcurrentEffect, ContextShift, IO}

object Fs2_10AsyncEffMult extends App {
  type Row = List[String]
  type RowOrError = Either[Throwable, Row]

  trait CSVHandle {
    def withRows(cb: RowOrError => Unit): Unit
  }

  def rows[F[_]](h: CSVHandle)(implicit F: ConcurrentEffect[F], cs: ContextShift[F]): Stream[F,Row] = {
    for {
      q <- Stream.eval(Queue.unbounded[F, Option[RowOrError]])
      _ <- Stream.eval { F.delay {
        def enqueue(v: Option[RowOrError]): Unit = F.runAsync(q.enqueue1(v))(_ => IO.unit).unsafeRunSync

        // Fill the data
        h.withRows(e => enqueue(Some(e)))
        // Upon returning from withRows signal that our stream has ended.
        enqueue(None)
      } }
      // unNoneTerminate halts the stream at the first `None`.
      // Without it the queue would be infinite.
      row <- q.dequeue.unNoneTerminate.rethrow
    } yield row
  }
}
