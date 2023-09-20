package es68

import cats.Functor
import cats.implicits.{catsSyntaxOptionId, toFunctorOps}
import com.sksamuel.elastic4s.searches.SearchRequest
import com.sksamuel.elastic4s.searches.sort.Sort
import fs2.{Chunk, Stream}

trait ESStreamSupportFs2 {

  def mkFs2Stream[F[_]: Functor, A](
      request: SearchRequest,
      sortFields: Seq[Sort],  // we are about to paginate, so we need consistent sorting
      afterFn: A => Seq[Any], // we need the extractor function to be used in `es.searchAfter(...)`
      bufferSize: Int = 1024  // page size
    )(esExecute: SearchRequest => F[Seq[A]]
    ): Stream[F, A] = {

    val request0: SearchRequest = request
      .sortBy(sortFields)
      .size(bufferSize)

    Stream.unfoldChunkEval(request0) { request: SearchRequest =>
      esExecute(request)
        .map {
          case xs if xs.isEmpty => None // no data
          case xs               =>      // data
            val afterClause = afterFn(xs.last)
            val afterQuery  = request.searchAfter(afterClause)
            (Chunk.from(xs), afterQuery).some
        }
    }
  }

}
