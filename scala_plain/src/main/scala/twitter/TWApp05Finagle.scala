package twitter

import scala.concurrent.Future

object TWApp05Finagle {
  trait Service[-Req, +Rep] extends (Req => Future[Rep])

  trait Filter[-ReqIn, +RepOut, +ReqOut, -RepIn] extends ((ReqIn, Service[ReqOut, RepIn]) => Future[RepOut]) {
    def andThen[Req2, Rep2](next: Filter[ReqOut, RepIn, Req2, Rep2]) =
      new Filter[ReqIn, RepOut, Req2, Rep2] {
        def apply(request: ReqIn, service: Service[Req2, Rep2]) = {
          Filter.this.apply(request, new Service[ReqOut, RepIn] {
            def apply(request: ReqOut): Future[RepIn] = next(request, service)
          })
        }
      }
  }
}
