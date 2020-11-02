package nomicon.ch02layer

import Domain.{User, UserId}
import Services.UserRepo
import zio.{IO, Layer, UIO, ZIO, ZLayer}

import scala.collection.mutable

trait DBError

object UserRepo {

  trait Service {
    def getUser(userId: UserId): IO[DBError, Option[User]]
    def createUser(user: User): IO[DBError, Unit]
  }

  val inMemory: Layer[Nothing, UserRepo] = ZLayer.succeed(
    new Service {
      private val db: mutable.Map[UserId, User] = mutable.Map.empty
      def getUser(userId: UserId): IO[DBError, Option[User]] = UIO(db.get(userId))
      def createUser(user: User): IO[DBError, Unit] = UIO(db.put(user.id, user))
    }
  )

  def getUser(userId: UserId): ZIO[UserRepo, DBError, Option[User]] = ZIO.accessM(ur => ur.get.getUser(userId))
  def createUser(user: User): ZIO[UserRepo, DBError, Unit] = ZIO.accessM(ur => ur.get.createUser(user))
}
