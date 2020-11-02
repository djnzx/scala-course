package nomicon.ch02layer

import zio.console.Console
import zio._

import scala.collection.mutable

/**
  * Modules, Layers
  * https://zio.dev/docs/howto/howto_use_layers
  * https://scala.monster/welcome-zio/#summary
  */
object ZLayerPlayground extends App {

  trait DBError
  case class UserId(id: Int) extends AnyVal
  case class User(id: UserId, name: String)

  type UserRepo = Has[UserRepo.Service]

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

  type Logging = Has[Logging.Service]

  object Logging {

    trait Service {
      def info(s: String): UIO[Unit]

      def error(s: String): UIO[Unit]
    }

    val consoleLogger: ZLayer[Console, Nothing, Logging] = ZLayer.fromFunction(console =>
      new Service {
        def info(s: String): UIO[Unit] = console.get.putStrLn(s"info - $s")

        def error(s: String): UIO[Unit] = console.get.putStrLn(s"error - $s")
      }
    )

    def info(s: String): URIO[Logging, Unit] = ZIO.accessM(_.get.info(s))

    def error(s: String): URIO[Logging, Unit] = ZIO.accessM(_.get.error(s))
  }

  val user2: User = User(UserId(123), "Tommy")
  val makeUser: ZIO[Logging with UserRepo, DBError, Unit] = for {
    _ <- Logging.info(s"inserting user") // URIO[Logging, Unit]
    _ <- UserRepo.createUser(user2) // ZIO[UserRepo, DBError, Unit]
    _ <- Logging.info(s"user inserted") // URIO[Logging, Unit]
    u1 <- UserRepo.getUser(UserId(123))
    u2 <- UserRepo.getUser(UserId(124))
    _  <- Logging.info(s"id 123: $u1")
    _  <- Logging.info(s"id 124: $u2")
  } yield ()
  
  val horizontal: ZLayer[Console, Nothing, Logging with UserRepo] = Logging.consoleLogger ++ UserRepo.inMemory
  val fullLayer: Layer[Nothing, Logging with UserRepo] = Console.live >>> horizontal
  val app: ZIO[Any, DBError, Unit] = makeUser.provideLayer(fullLayer)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
