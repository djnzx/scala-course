package gettingstarted

/**
  * https://www.youtube.com/watch?v=6A1SA5Be9qw
  */
import zio._
import zio.clock.Clock
import zio.console._
import zio.duration.durationInt
import zio.test._
import zio.test.Assertion._
import zio.test.environment._

/** core, representation */
object GS1 extends zio.App {
  // must have Nothing. It means all errors are caught.
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    console.putStrLn("Hello World")
      .as(ExitCode.success)
}

/** running */
object GS2 extends scala.App {
  val sayHello: ZIO[Any, Nothing, Unit] = ZIO.effectTotal(println("Hello, NY!"))

  Runtime.default.unsafeRun(sayHello)
}

/** retry policies */
object GS3 {
  // base action
  val tweets : ZIO[Any,            Throwable, List[String]] = ???
  // retried action
  val retried: ZIO[Any with Clock, Throwable, List[String]] = tweets.retry(Schedule.exponential(1.second))
}

/** error handling */
object GS4 {
  trait Config
  val defaultConfig: Config = new Config {}
  
  sealed trait ConfigError
  final case class IOError(msg: String) extends ConfigError
  final case class ParseError(msg: String) extends ConfigError
  
  def readFile(path: String): ZIO[Any, IOError, String] = ???
  def parseConfig(s: String): ZIO[Any, ParseError, Config] = ???
  
  def readConfig(path: String): ZIO[Any, ConfigError, Config] = readFile(path).flatMap(parseConfig)
  val config: ZIO[Any, Nothing, Config] = readConfig("config.json").orElseSucceed(defaultConfig)
}

/** fork / join */
object GS5 {
  val main: ZIO[Any, Nothing, Boolean] = ???
  val job: ZIO[Any, NumberFormatException, Int] = ???
  val forked: ZIO[Any, Nothing, Fiber.Runtime[NumberFormatException, Int]] = job.fork
  val cont: ZIO[Either[Any, Any], Nothing, Any] = forked.join(main)

  /** bracket, try/catch */
  val finalizer: ZIO[Any, Nothing, Nothing] = ???
  cont.ensuring(finalizer)

  /** interrupt, resource-safe ! */
  val interrupted: ZIO[Any, Nothing, Exit[NumberFormatException, Int]] = forked.flatMap(_.interrupt)
}

/** parallelism */
object GS6 {
  trait User
  val ids: List[Int] = ???
  
  def getUserById(id: Int): ZIO[Any, Throwable, User] = ???
  /** but actually, foreach is map */
  val getAll1: ZIO[Any, Throwable, List[User]] = ZIO.foreachPar(ids)(getUserById)
  val getAll2: ZIO[Any, Throwable, List[User]] = ZIO.foreachParN(4)(ids)(getUserById)
}

/** race */
object GS7 {
  trait Result
  val getDataFromLocation1: ZIO[Any, Throwable, List[Result]] = ???
  val getDataFromLocation2: ZIO[Any, Throwable, List[Result]] = ???

  /**
    * - return first succeed
    * - automatically interrupts loser
    */
  val result: ZIO[Any, Throwable, List[Result]] = 
    getDataFromLocation1.race(getDataFromLocation2)
  
}

/** managed resources */
object GS8 {
  trait Connection
  trait UserRepo
  /** managed resource using */
  val conn: ZManaged[Any, Throwable, Connection] = ???
  def pg(conn: Connection): ZManaged[Any, Throwable, UserRepo] = ???
  /** chaining */
  val userRepo: ZManaged[Any, Throwable, UserRepo] = conn.flatMap(pg)
}

/**
  * ZIO.provide:     ZIO[R,E,A] => IO[E,A]
  * ZIO.environment:          R => ZIO[R, Nothing, R]
  */
object GS9 {
}

/**
  * Ref       -> Atomic reference
  * Promise   -> Single value
  * Queue     -> Multiple Value
  * Semaphore -> Concurrency
  * Schedule  -> repeats and retries
  */

/**
  * 1:06
  * 
  * TRef, STM
  */
