package es68

import cats.implicits.catsSyntaxOptionId
import cats.implicits.none
import zio.Chunk
import zio.ZIO
import zio.stream.ZStream

/** this trait gives ability to stream everything
  * just by having following:
  *
  * - value of initial request of type Q
  *
  * - function dataFetcher {{{Q => Future[Seq[A]]}}}
  *
  * - function modifier {{{afterFn: (Q, A) => Q}}} how to modify query (AFTER clause) having last record
  */
trait StreamSupportZio {

  /** not aware about page size, therefore can't detect last page */
  def mkZioStream[A, Q](
      request: Q,
      afterFn: (Q, A) => Q
    )(dataFetcher: Q => ZIO[Any, Throwable, Seq[A]]
    ): ZStream[Any, Throwable, A] =
    ZStream
      .unfoldChunkZIO(request) { request =>
        dataFetcher(request)
          .map {
            case xs if xs.isEmpty => none
            case xs               =>
              val afterQ = afterFn(request, xs.last)
              (Chunk.fromIterable(xs), afterQ).some
          }
      }

  /** aware about page size, therefore can detect last page and eliminate last call */
  def mkZioStream[A, Q](
      request: Q,
      afterFn: (Q, A) => Q,
      pageSize: Int
    )(dataFetcher: Q => ZIO[Any, Throwable, Seq[A]]
    ): ZStream[Any, Throwable, A] =
    ZStream
      .paginateChunkZIO(request) { request =>
        dataFetcher(request)
          .map {
            case xs if xs.isEmpty           => (Chunk.empty, none)
            case xs if xs.length < pageSize => (Chunk.fromIterable(xs), none)
            case xs                         =>
              val afterQ = afterFn(request, xs.last)
              (Chunk.fromIterable(xs), afterQ.some)
          }
      }

}
