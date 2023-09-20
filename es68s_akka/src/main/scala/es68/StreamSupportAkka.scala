package es68

import akka.NotUsed
import akka.stream.scaladsl.Source
import cats.implicits.catsSyntaxOptionId
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/** this trait gives ability to stream everything
  * just by having following:
  *
  * - value of initial request of type Q
  *
  * - function dataFetcher {{{Q => Future[Seq[A]]}}}
  *
  * - function modifier {{{afterFn: (Q, A) => Q}}} how to modify query (AFTER clause) having last record
  */
trait StreamSupportAkka {

  /** not aware about page size, therefore can't detect last page
    * due to the lack of functionality Akka Streams we can't implement
    * last page-aware implementation
    */
  def mkAkkaStream[A, Q](
      request: Q,          // initial query, must be ORDERED and LIMITED as well
      afterFn: (Q, A) => Q // how to modify query (AFTER clause)
    )(dataFetcher: Q => Future[Seq[A]]
    )(implicit ec: ExecutionContext
    ): Source[A, NotUsed] =
    Source
      .unfoldAsync(request) { request =>
        dataFetcher(request)
          .map {
            case xs if xs.isEmpty => None // no data
            case xs               =>      // data
              val qAfter = afterFn(request, xs.last)
              (qAfter, xs).some
          }
      }
      .mapConcat(identity)

}
