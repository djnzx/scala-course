package doobiex

import cats.effect._
import doobie._
import doobie.implicits._

import java.util.UUID

object Doobie3 extends IOApp {

  case class CountryId(value: Int)
  case class CountryDetails(id: CountryId, name: String, pop: Option[Long], code: Option[UUID])

  def stringToUuid(s: String): UUID = UUID.fromString(s)
  def uuidToString(uuid: UUID): String = uuid.toString

  /** how to convert String to UUID (for reading purposes) */
  val uuidGet: Get[UUID] = Get[String].map(stringToUuid)

  /** how to convert UUID to String (for writing purposes) */
  val uuidPut: Put[UUID] = Put[String].contramap(uuidToString)

  /** Meta combines Get & Put */
  implicit val uuidMeta: Meta[UUID] = Meta[String].imap(stringToUuid)(uuidToString)

  val program1 = sql"select id, name, qty, code from t1"
    .query[CountryDetails]
    .to[List]

  def program2(name1: String, code1: UUID) =
    sql"insert into t1 (name, code) values ($name1, $code1::uuid)"
      .update
      .run

  val program = program2("Canada", UUID.randomUUID())

  val rIO = program.transact(xa[IO])

  override def run(args: List[String]): IO[ExitCode] = for {
    rI <- rIO
//    rs = rI.mkString("\n")
    rs = rI.toString
    _ <- putStrLn(rs)
  } yield ExitCode.Success

}
