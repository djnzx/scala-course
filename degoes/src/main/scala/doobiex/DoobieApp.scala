package doobiex

import pureconfig._
import pureconfig.generic.auto._
import pureconfigx.{AppConfig, PgConfig}

import doobie._
import doobie.implicits._
import cats.effect.IO

import pprint.{pprintln => println}

import scala.concurrent.ExecutionContext

object DoobieApp extends App {
  val appConf: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]
  
  implicit val cs = IO.contextShift(ExecutionContext.global)
  def trans(c: PgConfig) = 
    Transactor.fromDriverManager[IO](c.driver, c.url, c.user, c.password)
    
  val xa = trans(appConf.db)

  case class Author(id: Int, name: String)

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
