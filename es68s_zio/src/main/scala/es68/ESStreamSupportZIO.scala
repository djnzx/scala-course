package es68

import cats.implicits.catsSyntaxOptionId
import com.sksamuel.elastic4s.searches.SearchRequest
import com.sksamuel.elastic4s.searches.sort.Sort
import zio.ZIO
import zio.stream.ZStream

trait ESStreamSupportZIO {

  def mkZioStream[A](
      request: SearchRequest,
      sortFields: Seq[Sort],  // we are about to paginate, so we need consistent sorting
      afterFn: A => Seq[Any], // we need the extractor function to be used in `es.searchAfter(...)`
      bufferSize: Int = 1024  // page size
    )(esExecute: SearchRequest => ZIO[Any, Throwable, Seq[A]]
    ): ZStream[Any, Throwable, A] = {

    /** initial request we start from */
    val request0: SearchRequest = request
      .sortBy(sortFields)
      .size(bufferSize)

    ZStream
      .paginateZIO(request0) { request: SearchRequest =>
        esExecute(request)
          .map {
            case xs if xs.isEmpty             => (xs, None) // no data
            case xs if xs.length < bufferSize => (xs, None) // last page
            case xs                           =>
              val afterClause = afterFn(xs.last)
              val afterQuery  = request.searchAfter(afterClause)
              (xs, afterQuery.some) // normal page
          }
      }
      .flatMap(xs => ZStream.fromIterable(xs))
  }

}
