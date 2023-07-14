package doobiex

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import doobie._
import doobie.implicits._
import doobiex.Db.driver
import doobiex.Db.pass
import doobiex.Db.url
import doobiex.Db.user

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

object Conn extends IOApp {

  val xa = Transactor.fromDriverManager[IO](driver, url, user, pass)

  case class Row(id: String, title: String, qty: Int)

//  implicit val put: Put[Row] = Put[Row].contramap(_.asJson)
//  implicit val get: Get[Row] = Get[Row].temap(_.as[Row].left.map(_.show))

  implicit val uuidMeta: Meta[UUID] =
    Meta[String].imap[UUID](UUID.fromString)(_.toString)

  def find(n: String) =
    sql"select id, title, qty from scala_onboarding where id = $n".query[(String, String, Int)].option

//  def find(n: UUID): ConnectionIO[Option[Row]] =
//    sql"select id, title, qty from scala_onboarding where id = $n".query[Row].option

  override def run(args: List[String]): IO[ExitCode] =
    "3f8afb74-91c7-4738-976d-25e4caf66e74"
//      .pipe(UUID.fromString)
      .pipe(find)
      .transact(xa)
      .map(_.foreach(println(_)))
      .as(ExitCode.Success)
}
