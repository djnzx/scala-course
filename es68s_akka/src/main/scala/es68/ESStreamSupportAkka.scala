package es68

import akka.NotUsed
import akka.stream.scaladsl.Source
import cats.implicits.catsSyntaxOptionId
import com.sksamuel.elastic4s.searches.SearchRequest
import com.sksamuel.elastic4s.searches.sort.Sort
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

trait ESStreamSupportAkka {

  def mkAkkaStream[A](
      request: SearchRequest,
      sortFields: Seq[Sort],  // we are about to paginate, so we need consistent sorting
      afterFn: A => Seq[Any], // we need the extractor function to be used in `es.searchAfter(...)`
      bufferSize: Int = 1024  // page size
    )(esExecute: SearchRequest => Future[Seq[A]]
    )(implicit ec: ExecutionContext
    ): Source[A, NotUsed] = {

    val request0: SearchRequest = request
      .sortBy(sortFields)
      .size(bufferSize)

    Source
      .unfoldAsync(request0) { request: SearchRequest =>
        esExecute(request)
          .map {
            case xs if xs.isEmpty => None // no data
            case xs               =>      // data
              val afterClause = afterFn(xs.last)
              val afterQuery  = request.searchAfter(afterClause)
              (afterQuery, xs).some
          }
      }
      .mapConcat(identity)
  }

}
