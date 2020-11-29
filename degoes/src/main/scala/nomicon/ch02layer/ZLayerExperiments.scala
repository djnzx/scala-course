package nomicon.ch02layer

import domain.Domain.{User, UserId}
import services.Aliases.{Configuration, Logging, UserRepo}
import services.{Configuration, DBError, Logging, UserRepo}
import zio.console.Console
import zio._

/**
  * Modules, Layers
  * https://zio.dev/docs/howto/howto_use_layers
  * https://scala.monster/welcome-zio/#summary
  */
object ZLayerExperiments extends App {

  val user: User = User(UserId(123), "Tommy")
  
  val appWithDeps: ZIO[Configuration with Logging with UserRepo, DBError, Unit] = for {
    c  <- Configuration.conf
    _  <- Logging.info(c.toString)
    _  <- Logging.info(s"inserting user")
    _  <- UserRepo.createUser(user)
    _  <- Logging.info(s"user inserted")
    u1 <- UserRepo.getUser(UserId(123))
    u2 <- UserRepo.getUser(UserId(124))
    _  <- Logging.info(s"id 123: $u1")
    _  <- Logging.info(s"id 124: $u2")
  } yield ()

  /** horizontal composition: requires: Console, will produce Logging + UserRepo */
  val composed = Logging.consoleLogger ++ UserRepo.inMemory ++ Configuration.file
  /** full layer constructed by passing console to the composed layer */
  val fullLayer = Console.live >>> composed
  
  /** build the app without dependencies by providing layer with deps */
  val appNoDeps: ZIO[Any, DBError, Unit] = appWithDeps.provideLayer(fullLayer)
  val appExCaught: ZIO[Console, Nothing, Unit] = appNoDeps.catchAll((x: DBError) => console.putStrLn(s"Error caught $x"))
  
  val app: URIO[Console, ExitCode] = appExCaught.exitCode

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app
}
