package doobiex

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.transactor.Strategy

/** The PROBLEM
  *
  * if you expose `F[_]`            - you CAN NOT run in transaction, since you ERASED the knowledge of fact that was a DB
  *                                 - you can hide `Transactor` in a constructor for different implementation (live, test)
  * if you expose `ConnectionIO[_]` - you CAN run in transaction, since `ConnectionIO` is a free monad, and run `transact` later
  *                                 - you need to provide custom interpreter for your
  *
  * https://blog.softwaremill.com/testing-doobie-programs-425517c1c295
  */
object DoobieTransaction extends IOApp.Simple {

  // belongs to repo1
  def select1: ConnectionIO[Long]          = sql"select 1 from T1".query[Long].unique
  // belongs to repo2
  def insert2(x: Long): ConnectionIO[Unit] = sql"insert into T2 (f) values ($x)".update.run.void

  // actually Free[ConnectionOp, Unit], will require
  def combined: ConnectionIO[Unit] = for {
    x <- select1
    _ <- insert2(x)
  } yield ()

  // real
  lazy val xa: Transactor[IO] = Transactor.fromDriverManager[IO]("driver", "url", "user", "pass")
  lazy val ioREAL: IO[Unit]   = combined.transact(xa)

  // test
  val waio: WeakAsync[IO] = WeakAsync.doobieWeakAsyncForAsync(Async[IO])
  val interpret           = KleisliInterpreter[IO](waio).ConnectionInterpreter

  val xaTEST: Transactor[IO] = Transactor(
    (),
    (_: Unit) => Resource.pure(null),
    interpret,
    Strategy.void
  )
//  lazy val ioTEST: IO[Unit]  = combined.transact(xaTEST)


  def get1: ConnectionIO[Long] = {
    println("getting")
    1L.pure[ConnectionIO]
  }
  def put1(x: Long): ConnectionIO[Unit] = {
    println("putting")
    ().pure[ConnectionIO]
  }

  val combT: ConnectionIO[Unit] = for {
    x <- get1
    _ <- put1(x)
  } yield ()

  val r: IO[Unit] = combT.transact(xaTEST)

  override def run: IO[Unit] = r >> IO.println(1)

}
