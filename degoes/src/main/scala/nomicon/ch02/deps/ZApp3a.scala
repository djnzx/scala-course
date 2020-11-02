package nomicon.ch02.deps

import zio._
import zio.console.Console

import scala.collection.mutable

/**
  * Modules, Layers
  * https://zio.dev/docs/howto/howto_use_layers
  * https://scala.monster/welcome-zio/#summary
  */
object ZApp3a extends App {

  case class UserId(id: Int) extends AnyVal
  case class User(id: UserId, name: String)

  trait DBConnection {
    def create(u: User): Unit
    def get(id: UserId): Option[User]
  }
  
  val db = new DBConnection {
    private val db: mutable.Map[UserId, User] = mutable.Map.empty
    override def create(u: User): Unit = {
      db.put(u.id, u)
    }
    override def get(id: UserId): Option[User] = db.get(id)
  }

  /**
    * step 1
    * this is terribly wrong,
    * we have external dependency,
    * and signature doesn't tell us about that
    * we can't rely on it
    */
  def getUser0(userId: UserId): ZIO[Any, Nothing, Option[User]] = UIO(db.get(userId))
  def createUser0(name: User): URIO[Any, Unit] = UIO(db.create(name))

  /**
    * step 2
    * represent working with repository
    * via currying
    */
  def getUser1(userId: UserId)(db: DBConnection) = db.get(userId)
  def createUser1(name: User)(db: DBConnection) = db.create(name)

  def getUser(userId: UserId): ZIO[DBConnection, Nothing, Option[User]] = UIO(db.get(userId))
  def createUser(name: User): ZIO[DBConnection, Nothing, Unit] = UIO(db.create(name))

  val user = User(UserId(123), "Jim")
  
  val created: ZIO[DBConnection, DBError, Boolean] = for {
    maybeUser <- getUser(UserId(1234))
    res       <- maybeUser.fold(
                    createUser(user).as(true)
                  )(
                    _ => ZIO.succeed(false))
  } yield res

  trait DBError

  type UserRepo = Has[UserRepo.Service]
  object UserRepo {
    // our contract WITHOUT dependency
    trait Service {
      def getUser(userId: UserId): IO[DBError, Option[User]]
      def createUser(user: User): IO[DBError, Unit]
    }

    // Layer is an implementation
    val inMemory: Layer[Nothing, UserRepo] = ZLayer.succeed(
      new Service {
        private val db: mutable.Map[UserId, User] = mutable.Map.empty

        def getUser(userId: UserId): IO[DBError, Option[User]] = UIO(db.get(userId))
        def createUser(user: User): IO[DBError, Unit] = UIO(db.put(user.id, user))
      }
    )
    
    // our implementation... WITH DEPENDENCY but with different signatures
    def getUser(userId: UserId): ZIO[UserRepo, DBError, Option[User]] =
      ZIO.accessM(ur => ur.get.getUser(userId))

    def createUser(user: User): ZIO[UserRepo, DBError, Unit] =
      ZIO.accessM(ur => ur.get.createUser(user))
  }
  type Logging = Has[Logging.Service]
  object Logging {
    trait Service {
      def info(s: String): UIO[Unit]
      def error(s: String): UIO[Unit]
    }

    import zio.console.Console
    //           (f: A => B): ZLayer[A, Nothing, Has[B]]
    val consoleLogger: ZLayer[Console, Nothing, Logging] = ZLayer.fromFunction(console =>
      
      new Service {
        def info(s: String): UIO[Unit]  = console.get.putStrLn(s"info - $s")
        def error(s: String): UIO[Unit] = console.get.putStrLn(s"error - $s")
      }
    )

    //accessor methods
    def info(s: String): URIO[Logging, Unit] =
      ZIO.accessM(_.get.info(s))

    def error(s: String): URIO[Logging, Unit] =
      ZIO.accessM(_.get.error(s))
  }

  val user2: User = User(UserId(123), "Tommy")
  val makeUser: ZIO[Logging with UserRepo, DBError, Unit] = for {
    _ <- Logging.info(s"inserting user")  // URIO[Logging, Unit]
    _ <- UserRepo.createUser(user2)       // ZIO[UserRepo, DBError, Unit]
    _ <- Logging.info(s"user inserted")   // URIO[Logging, Unit]
  } yield ()

  // compose horizontally
  val horizontal: ZLayer[Console, Nothing, Logging with UserRepo] = Logging.consoleLogger ++ UserRepo.inMemory

  // fulfill missing deps, composing vertically
  val fullLayer: Layer[Nothing, Logging with UserRepo] = Console.live >>> horizontal

  
  val withDB: ZIO[DBConnection, DBError, Boolean] => IO[DBError, Boolean] = ZIO.provide(db)
  val app4: IO[DBError, Boolean] = withDB(created)
  val app2: IO[DBError, Boolean] = ZIO.provide(db)(created)
  val app3: IO[DBError, Boolean] = created.provide(db)

  val app: ZIO[Any, DBError, Unit] = makeUser.provideLayer(fullLayer)
  
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
