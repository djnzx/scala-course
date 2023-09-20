package es68

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.sksamuel.elastic4s.searches.SearchRequest
import com.sksamuel.elastic4s.searches.sort.Sort
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

trait ESStreamSupportAkka extends StreamSupportAkka {

  def mkEsAkkaStream[A](
      request: SearchRequest,
      sortFields: Seq[Sort],  // we are about to paginate, so we need consistent sorting
      afterFn: A => Seq[Any], // we need the extractor function to be used in `es.searchAfter(...)`
      bufferSize: Int = 1024  // page size
    )(esExecute: SearchRequest => Future[Seq[A]]
    )(implicit ec: ExecutionContext
    ): Source[A, NotUsed] = {

    val initial: SearchRequest = request
      .sortBy(sortFields)
      .size(bufferSize)

    def reqModifyFn(q: SearchRequest, last: A): SearchRequest = q.searchAfter(afterFn(last))

    mkAkkaStream(initial, reqModifyFn)(esExecute)
  }

}
