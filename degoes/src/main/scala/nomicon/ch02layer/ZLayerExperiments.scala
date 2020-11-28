package nomicon.ch02layer

import domain.Domain.{User, UserId}
import services.Aliases.{Logging, UserRepo}
import services.{DBError, Logging, UserRepo}
import zio.console.Console
import zio._

/**
  * Modules, Layers
  * https://zio.dev/docs/howto/howto_use_layers
  * https://scala.monster/welcome-zio/#summary
  */
object ZLayerExperiments extends App {

  val user: User = User(UserId(123), "Tommy")
  
  val appWithDeps: ZIO[Logging with UserRepo, DBError, Unit] = for {
    _  <- Logging.info(s"inserting user") // ZIO[Logging,  Nothing, Unit]
    _  <- UserRepo.createUser(user)       // ZIO[UserRepo, DBError, Unit]
    _  <- Logging.info(s"user inserted")  // ZIO[Logging,  Nothing, Unit]
    u1 <- UserRepo.getUser(UserId(123))   // ZIO[UserRepo, DBError, Option[User]]
    u2 <- UserRepo.getUser(UserId(124))
    _  <- Logging.info(s"id 123: $u1")
    _  <- Logging.info(s"id 124: $u2")
  } yield ()

  /** layer 1: requires: Nothing => produces: UserRepo */
  val userRepo: ZLayer[Any, Nothing, UserRepo] = UserRepo.inMemory
  /** layer 2: requires: Console => produces: Logging */
  val consoleLog: ZLayer[Console, Nothing, Logging] = Logging.consoleLogger
  
  /** horizontal composition: requires: Console, will produce Logging + UserRepo */
  val composed: ZLayer[Console, Nothing, Logging with UserRepo] = consoleLog ++ userRepo
  /** full layer constructed by passing console to the composed layer */
  val fullLayer: ZLayer[Any, Nothing, Logging with UserRepo] = Console.live >>> composed
  
  /** build the app without dependencies by providing layer with deps */
  val appNoDeps: ZIO[Any, DBError, Unit] =
    appWithDeps.provideLayer(fullLayer)

  val appExCaught: ZIO[Console, Nothing, Unit] =
    appNoDeps.catchAll(_ => console.putStrLn("Error caught"))

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = appExCaught.exitCode
}
