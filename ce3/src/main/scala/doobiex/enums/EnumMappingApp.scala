package doobiex.enums

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobiex.xa
import java.util.UUID

/** Postgres ENUM internal implementation
  * syntax:
  * {{{
  * create type message_type AS ENUM('trace', 'debug', 'info', 'warn', 'error', 'fatal')
  * }}}
  * motivation:
  *  - internal state just 4-byte integer
  *  - ordering works out of the box
  */
object EnumMappingApp extends IOApp.Simple {

  /** required to derive Get[MyRow]
    * import MetaInstances._
    */

  import doobiex.uuid.InstancesUuidMeta._

  case class MyRow(id: UUID, typ: MessageType, msg: String)

  val program: ConnectionIO[List[MyRow]] = sql"select id, typ, msg from messages"
    .query[MyRow]
    .to[List]

  val io: IO[List[MyRow]] = program.transact(xa[IO])

  override def run: IO[Unit] =
    program
      .transact(xa[IO])
      .flatMap(_.traverse_(x => IO(pprint.pprintln(x))))
}
