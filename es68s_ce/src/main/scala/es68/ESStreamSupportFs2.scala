package es68

import cats.Functor
import cats.implicits.catsSyntaxOptionId
import cats.implicits.toFunctorOps
import com.sksamuel.elastic4s.searches.SearchRequest
import com.sksamuel.elastic4s.searches.sort.Sort
import fs2.Chunk
import fs2.Stream

trait ESStreamSupportFs2 extends StreamSupportFs2 {

  def mkEsFs2Stream[F[_]: Functor, A](
      request: SearchRequest,
      sortFields: Seq[Sort],  // we are about to paginate, so we need consistent sorting
      afterFn: A => Seq[Any], // we need the extractor function to be used in `es.searchAfter(...)`
      pageSize: Int = 1024    // page size
    )(esExecute: SearchRequest => F[Seq[A]]
    ): Stream[F, A] = {

    val initial: SearchRequest = request
      .sortBy(sortFields)
      .size(pageSize)

    def reqModifyFn(q: SearchRequest, last: A): SearchRequest = q.searchAfter(afterFn(last))

    mkFs2Stream(initial, reqModifyFn, pageSize)(esExecute)
  }

}
