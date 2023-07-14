package doobiex

import cats.effect.{ExitCode, IO, IOApp}
import doobie._
import doobie.implicits._
import nomicon.ch02layer.domain.Domain.{User, UserId}
import pprint.{pprintln => println}
import pureconfig._
import pureconfigx.{AppConfig, PgConfig}
import pureconfig.generic.auto._

/**
  * https://tpolecat.github.io/doobie/
  */
object DoobieApp extends IOApp.Simple {
  val conf: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]

  def trans(c: PgConfig) = Transactor.fromDriverManager[IO](c.driver, c.url, c.user, c.password)

  val xa = trans(conf.db)

  def find(id: Int) = sql"""select id, name from "user" where id = $id""".query[User].option

  def create(u: User) = sql"""insert into "user" (id, name) values (${u.id}, ${u.name})""".update.run
  
  val app = for {
    u1 <- find(123).transact(xa)
    u2 <- find(124).transact(xa)
    _  <- IO.delay { println(u1) }
    _  <- IO.delay { println(u2) }
    _  <- create(User(UserId(125),"Jim")).transact(xa)
  } yield ()

  override def run: IO[Unit] = app
}
