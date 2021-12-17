package doobiex

import cats.Show
import cats.data.NonEmptyList
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import io.circe.Json
import io.circe.jawn._
import org.postgresql.util.PGobject

import java.util.UUID

/** Json Experiments */
object Doobie4 extends IOApp {

  def stringToUuid(s: String): UUID = UUID.fromString(s)
  def uuidToString(uuid: UUID): String = uuid.toString
  implicit val uuidMeta: Meta[UUID] = Meta[String].imap(stringToUuid)(uuidToString)

  implicit val showPGobject: Show[PGobject] = Show.show(_.getValue)

  implicit val jsonGet: Get[Json] =
    Get
      .Advanced
      .other[PGobject](NonEmptyList.of("json"))
      .temap[Json] { o: PGobject =>
        parse(o.getValue)
          .leftMap(_.show)
      }

  case class CountryId(value: Int)
  case class Country(id: CountryId, name: String, code: Option[UUID], extra: Option[Json])

  def program1 =
    sql"select id, name, code, extra from t1"
      .query[Country]
      .to[List]

  val program = program1

  val rIO = program.transact(xa[IO])

  override def run(args: List[String]): IO[ExitCode] = for {
    rI <- rIO
    rs = rI.mkString("\n")
    _ <- putStrLn(rs)
  } yield ExitCode.Success

}
