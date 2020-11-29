package doobiex

import pureconfig._
import pureconfig.generic.auto._
import pureconfigx.{AppConfig, PgConfig}
import doobie._
import doobie.implicits._
import cats.effect.{ContextShift, IO}
import nomicon.ch02layer.domain.Domain.{User, UserId}
import pprint.{pprintln => println}

import scala.concurrent.ExecutionContext

/**
  * https://tpolecat.github.io/doobie/
  */
object DoobieApp extends App {
  val appConf: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]

  def trans(c: PgConfig)(implicit cs: ContextShift[IO]) =
    Transactor.fromDriverManager[IO](c.driver, c.url, c.user, c.password)
  
  val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  // connection on which we will run our SQL
  val xa = trans(appConf.db)(cs)

  def find(id: Int) =
    sql"""select id, name from "user" where id = $id""".query[User].option

  def create(u: User) =
    sql"""insert into "user" (id, name) values (${u.id}, ${u.name})""".update.run
  
  val app = for {
    u1 <- find(123).transact(xa)
    u2 <- find(124).transact(xa)
    _  <- IO.delay { println(u1) }
    _  <- IO.delay { println(u2) }
    _  <- create(User(UserId(125),"Jim")).transact(xa)
  } yield ()
  
  app.unsafeRunSync
}
