package mongox

import cats.Eval
import cats.effect.Async
import cats.effect.Blocker
import cats.effect.ContextShift
import cats.effect.Resource
import cats.effect.Sync
import cats.implicits._
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.Observable

import scala.concurrent.Future

class MongoConnector[F[_]: Async: ContextShift](
    blocker: Blocker,
    client: MongoClient,
    database: String) {

  private val db: Eval[MongoDatabase] = Eval.later(client.getDatabase(database))

  private def withDBFuture[A](f: MongoDatabase => Future[A]): F[A] =
    blocker.blockOn(Async.fromFuture(Sync[F].delay(f(db.value))))

  def queryOption[A](f: MongoDatabase => Observable[A]): F[Option[A]] =
    withDBFuture(f(_).headOption())

  def queryOne[A](metaName: String)(f: MongoDatabase => Observable[A]): F[A] =
    queryOption(f).flatMap { opt: Option[A] =>
      Sync[F].fromOption(
        opt,
        new Exception(s"Error during running mongodb future request $metaName"),
      )
    }

  def queryMany[T](f: MongoDatabase => Observable[T]): F[List[T]] =
    withDBFuture(f(_).toFuture()).map(_.toList)

}

object MongoConnector {

  def resource[F[_]](
      url: String,
      db: String,
    )(
      implicit F: Async[F],
      CS: ContextShift[F],
      blocker: Blocker,
    ): Resource[F, MongoConnector[F]] = {
    Resource
      .make[F, MongoClient](
        Sync[F].delay(MongoClient(url)).flatMap { c =>
          Async.fromFuture(Sync[F].delay(c.listDatabaseNames().head())).as(c)
        },
      )(client => Sync[F].delay(client.close()))
      .map(new MongoConnector[F](blocker, _, db))
  }

  def resource[F[_]: Async: ContextShift](
      blocker: Blocker,
      mongoConfig: MongoConfig,
    ): Resource[F, MongoConnector[F]] = {
    implicit val b: Blocker = blocker
    resource(mongoConfig.url, mongoConfig.db)
  }

}
