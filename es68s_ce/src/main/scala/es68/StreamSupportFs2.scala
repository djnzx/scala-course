package es68

import cats.Functor
import cats.implicits.catsSyntaxOptionId
import cats.implicits.toFunctorOps
import fs2.Chunk
import fs2.Stream

/** this trait gives ability to stream everything
  * just by having following:
  *
  * - value of initial request of type Q
  *
  * - function dataFetcher {{{Q => Future[Seq[A]]}}}
  *
  * - function modifier {{{afterFn: (Q, A) => Q}}} how to modify query (AFTER clause) having last record
  */
trait StreamSupportFs2 {

  /** not aware about page size, therefore can't detect last page */
  def mkFs2Stream[F[_]: Functor, A, Q](
      request: Q,
      afterFn: (Q, A) => Q
    )(dataFetcher: Q => F[Seq[A]]
    ): Stream[F, A] =
    Stream.unfoldChunkEval(request) { request =>
      dataFetcher(request)
        .map {
          case xs if xs.isEmpty => None // no data
          case xs               =>      // data
            val afterQ = afterFn(request, xs.last)
            (Chunk.from(xs), afterQ).some
        }
    }

  /** aware about page size, therefore can detect last page and eliminate last call */
  def mkFs2Stream[F[_]: Functor, A, Q](
      request: Q,
      afterFn: (Q, A) => Q,
      pageSize: Int
    )(dataFetcher: Q => F[Seq[A]]
    ): Stream[F, A] =
    Stream
      .unfoldLoopEval(request) { request =>
        dataFetcher(request)
          .map {
            case xs if xs.isEmpty           => (xs, None) // no data
            case xs if xs.length < pageSize => (xs, None) // last page
            case xs                         =>            // normal page
              val afterQ = afterFn(request, xs.last)
              (xs, afterQ.some)
          }
      }
      .flatMap(xs => Stream.emits(xs))

}
