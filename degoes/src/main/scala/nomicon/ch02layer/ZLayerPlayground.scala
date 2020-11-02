package nomicon.ch02layer

import Services.{Logging, UserRepo}
import nomicon.ch02layer.Domain._
import zio.console.Console
import zio._

/**
  * Modules, Layers
  * https://zio.dev/docs/howto/howto_use_layers
  * https://scala.monster/welcome-zio/#summary
  */
object ZLayerPlayground extends App {

  val user2: User = User(UserId(123), "Tommy")
  val makeUser: ZIO[Logging with UserRepo, DBError, Unit] = for {
    _  <- Logging.info(s"inserting user") // ZIO[Logging,  Nothing, Unit]
    _  <- UserRepo.createUser(user2)      // ZIO[UserRepo, DBError, Unit]
    _  <- Logging.info(s"user inserted")  // ZIO[Logging,  Nothing, Unit]
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
