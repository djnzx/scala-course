package doobiex

import pureconfig._
import pureconfig.generic.auto._

import doobie._
import doobie.implicits._
import cats.effect.IO
import doobie.util.transactor.Transactor.Aux
import pureconfigx.AppConfig

import scala.concurrent.ExecutionContext

object DoobieApp extends App {
  val c: AppConfig = ConfigSource.default.loadOrThrow[AppConfig]
  
  implicit val cs = IO.contextShift(ExecutionContext.global)

  val xa = Transactor.fromDriverManager[IO](c.db.driver, c.db.url, c.db.user, c.db.password)

  case class Author(id: Int, name: String)

  def find(id: Int): ConnectionIO[Option[Author]] =
    sql"select id, name from author where id = $id".query[Author].option

  val r1 = find(123).transact(xa).unsafeRunSync
  val r2 = find(124).transact(xa).unsafeRunSync
  pprint.pprintln(r1)
  pprint.pprintln(r2)
}
