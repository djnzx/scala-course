package es68

import cats.MonadThrow
import cats.implicits._
import com.sksamuel.elastic4s.HitReader
import com.sksamuel.elastic4s.IndexAndType
import com.sksamuel.elastic4s.http._
import com.sksamuel.elastic4s.http.get.GetResponse
import com.sksamuel.elastic4s.http.get.MultiGetResponse
import com.sksamuel.elastic4s.http.search.SearchHit
import com.sksamuel.elastic4s.http.search.SearchResponse
import es68.ESClient.ApplicationException
import es68.ESClient.errors
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scala.language.higherKinds
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.util.control.NonFatal

/** IT'S NOT REDESIGN. IT JUST A PULL FROM THE JAR + MINOR REFACTORING */
// TODO: actually we don't use A, think about it
class ESClient[F[_]: MonadThrow](val nativeClient: ElasticClient) {

  implicit val log: Logger = LoggerFactory.getLogger(this.getClass)

  private def failureHandler[R](f: RequestFailure): F[R] =
    new RuntimeException(f.error.reason).raiseError[F, R]

  /** fundamental function to call Elastic Engine */
  def execute0[Q, R: Manifest](
      query: Q,
      handler: RequestFailure => F[R] = failureHandler[R] _
    )(implicit
      h: Handler[Q, R],
      f: Functor[F],
      ex: Executor[F]
    ): F[R] =
    nativeClient.execute[Q, R, F](query).flatMap {
      case s: RequestSuccess[R] => s.result.pure[F]
      case f: RequestFailure    => handler(f)
    }

  /** one of possible error handlers */
  private def logAndThrow[Q, R](q: Q, scalaMethodName: String): PartialFunction[Throwable, F[R]] = { case NonFatal(x) =>
    log.warn(s"Elasticsearch request: $q failed in method $scalaMethodName", x)
    ApplicationException(errors.InternalError, x).raiseError[F, R]
  }

  /** the same but provides custom logging */
  def execute[Q, R: Manifest](
      query: Q,
      scalaMethodName: String
    )(errorHandler: PartialFunction[Throwable, F[R]] = logAndThrow[Q, R](query, scalaMethodName)
    )(implicit
      h: Handler[Q, R],
      f: Functor[F],
      ex: Executor[F]
    ): F[R] =
    execute0(query).recoverWith(errorHandler)

  def close(): Unit = nativeClient.close()
}

object ESClient {

  /** ONE result manipulation */
  implicit class SearchHitOps(sh: SearchHit) {

    def toSeq[A: HitReader](implicit log: Logger): Option[A] =
      sh.safeTo[A].toOptionWarn

  }

  /** ONE result manipulation */
  implicit class GetResponseOps(rs: GetResponse) {

    def toSeq[A: HitReader](implicit log: Logger): Option[A] =
      rs.safeTo[A].toOptionWarn

  }

  /** MANY result manipulation */
  implicit class SearchResponseOps(sr: SearchResponse) {

    def toSeq[A: HitReader](implicit log: Logger): Seq[A] =
      sr.safeTo[A]
        .flatMap(_.toOptionWarn)

    def toTotalResult[A: HitReader](implicit log: Logger): TotalResult[A] =
      TotalResult(
        sr.safeTo[A].flatMap(_.toOptionWarn),
        sr.hits.total.toInt
      )

  }

  /** MANY result manipulation */
  implicit class MultiGetResponseOps(gr: MultiGetResponse) {

    def toSeq[A: HitReader](implicit log: Logger): Seq[A] =
      gr.items.toStream
        .map(_.safeTo[A])
        .flatMap(_.toOptionWarn)

  }

  /** IndexAndTypeFn based on brandId creator */
  trait IndexAndTypeFn extends (String => IndexAndType) {
    def apply(brandId: String): IndexAndType
  }

  /** elastic uuid key extractor */
  trait DocumentIdFn[A] extends (A => String) {
    def apply(entity: A): String
  }

  trait HasUuid[A] {
    def uuid(a: A): String
  }

  case class ApplicationException(message: String, exception: Throwable) extends RuntimeException

  case class TotalResult[A](data: Seq[A], total: Int)

  object errors {
    val InternalError = "error.internal"
  }

  implicit class TryOps2[A](t: Try[A]) {

    def toOptionWarn(
        implicit log: Logger
      ): Option[A] = t match {
      case Success(a) => a.some
      case Failure(x) => log.warn("Failed to deserialize entity. Cause: ", x); None
    }

  }

  def mkIndexAndType(brandId: String, indexSuffix: String, `type`: String): IndexAndType = {
    val index: String = s"$brandId$indexSuffix"
    IndexAndType(index, `type`)
  }

  def apply[F[_]: MonadThrow](nativeClient: ElasticClient): ESClient[F] =
    new ESClient[F](nativeClient)

  def apply[F[_]: MonadThrow](configFileName: String): ESClient[F] = {
    val props: ElasticProperties    = ElasticConfig.props(configFileName)
    val nativeClient: ElasticClient = ElasticClient(props)
    apply(nativeClient)
  }

}
