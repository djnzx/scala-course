package doobiex

import pureconfig._
import pureconfig.generic.auto._
import pureconfigx.{AppConfig, PgConfig}
import doobie._
import doobie.implicits._
import cats.effect.{ContextShift, IO}
import pprint.{pprintln => println}

import scala.concurrent.ExecutionContext

/**
  * https://tpolecat.github.io/doobie/
  */
object DoobieApp extends App {
  case class Author(id: Int, name: String)
  
  val appConf: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]

  def trans(c: PgConfig)(implicit cs: ContextShift[IO]) =
  Transactor.fromDriverManager[IO](c.driver, c.url, c.user, c.password)
  
  val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  // connection on which we will run our SQL
  val xa = trans(appConf.db)(cs)

  def find(id: Int): ConnectionIO[Option[Author]] =
    sql"select id, name from author where id = $id".query[Author].option

  val app = for {
    u1 <- find(123).transact(xa)
    u2 <- find(124).transact(xa)
    _  <- IO.delay { println(u1) }
    _  <- IO.delay { println(u2) }
  } yield ()
  
  app.unsafeRunSync
}
