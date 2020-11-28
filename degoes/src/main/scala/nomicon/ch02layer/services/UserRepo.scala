package nomicon.ch02layer.services

import nomicon.ch02layer.domain.Domain.{User, UserId}
import Aliases.UserRepo
import zio._

import scala.collection.mutable

trait DBError

object UserRepo {

  /** 
    * 1. interface 
    * without any knowledge about environment
    */
  trait Service {
    def createUser(user: User): ZIO[Any, DBError, Unit]
    def getUser(userId: UserId): ZIO[Any, DBError, Option[User]]
  }

  /**
    * 2. convert interface methods A => B to R => A => B
    * actually just implementing reader, requiring the env. (just reader)
    * to provide it later 
    */
  def createUser(user: User): ZIO[UserRepo, DBError, Unit] = ZIO.accessM[UserRepo](_.get.createUser(user))
  def getUser(userId: UserId): ZIO[UserRepo, DBError, Option[User]] = ZIO.accessM[UserRepo](_.get.getUser(userId))

  /**
    * 3. one of particular implementations represented as a Layer
    * {{{ ULayer[Has[Service]]
    * ULayer[UserRepo]
    * ZLayer[Any, Nothing, Has[Service]]
    * ZLayer[Any, Nothing, UserRepo]
    * Layer[Nothing, UserRepo]}}}
    */
  val inMemory: ZLayer[Any, Nothing, UserRepo] = ZLayer.succeed(new Service {
    private val db: mutable.Map[UserId, User] = mutable.Map.empty
    def createUser(user: User): IO[DBError, Unit] = UIO(db.put(user.id, user))
    def getUser(userId: UserId): IO[DBError, Option[User]] = UIO(db.get(userId))
  })  
    
}
