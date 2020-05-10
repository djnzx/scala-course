package pfps

import scala.util.control.NoStackTrace

object A036 {
  sealed trait Category

  sealed trait BusinessError extends NoStackTrace
  case object RandomError extends BusinessError

  trait Categories[F[_]] {
    def findAll: F[List[Category]]
  }

//  class LiveCategories[F[_]: MonadError[*[_], Throwable]: Random] extends Categories[F] {
//    def findAll: F[List[Category]] = Random[F].bool.ifM(
//      List.empty[Category].pure[F],
//      RandomError.raiseError[F, List[Category]]
//    )
//  }

}
