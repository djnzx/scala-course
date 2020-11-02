package nomicon.ch02layer.services

import nomicon.ch02layer.Domain.{User, UserId}
import nomicon.ch02layer.Services.UserRepo
import zio._

import scala.collection.mutable

trait DBError

object UserRepo {

  /** interface */
  trait Service {
    def getUser(userId: UserId): IO[DBError, Option[User]]
    def createUser(user: User): IO[DBError, Unit]
  }

  /** linking interface to implementation
    * can fail, so => accessM */
  def getUser(userId: UserId): ZIO[UserRepo, DBError, Option[User]] = ZIO.accessM(ur => ur.get.getUser(userId))
  def createUser(user: User): ZIO[UserRepo, DBError, Unit] = ZIO.accessM(ur => ur.get.createUser(user))

  /** real implementation, will be wired further */
  val inMemory: Layer[Nothing, UserRepo] = ZLayer.succeed(
    new Service {
      private val db: mutable.Map[UserId, User] = mutable.Map.empty
      def getUser(userId: UserId): IO[DBError, Option[User]] = UIO(db.get(userId))
      def createUser(user: User): IO[DBError, Unit] = UIO(db.put(user.id, user))
    }
  )

}
