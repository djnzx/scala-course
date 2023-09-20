package es68

import com.sksamuel.elastic4s.searches.SearchRequest
import com.sksamuel.elastic4s.searches.sort.Sort
import zio.ZIO
import zio.stream.ZStream

trait ESStreamSupportZio extends StreamSupportZio {

  def mkEsZioStream[A](
      request: SearchRequest,
      sortFields: Seq[Sort],  // we are about to paginate, so we need consistent sorting
      afterFn: A => Seq[Any], // we need the extractor function to be used in `es.searchAfter(...)`
      bufferSize: Int = 1024  // page size
    )(esExecute: SearchRequest => ZIO[Any, Throwable, Seq[A]]
    ): ZStream[Any, Throwable, A] = {

    /** initial request we start from */
    val initial: SearchRequest = request
      .sortBy(sortFields)
      .size(bufferSize)

    def reqModifyFn(q: SearchRequest, last: A): SearchRequest = q.searchAfter(afterFn(last))

    mkZioStream(initial, reqModifyFn, bufferSize)(esExecute)
  }

}
