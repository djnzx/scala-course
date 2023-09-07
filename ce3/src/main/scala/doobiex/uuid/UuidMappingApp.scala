package doobiex.uuid

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobiex.xa
import java.util.UUID

/** Postgres extensions to handle UUID generation
  * {{{
  * CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
  * }}}
  * it allows syntax
  * {{{
  * id uuid default uuid_generate_v4()
  * }}}
  */
object UuidMappingApp extends IOApp.Simple {

  /** required to derive Get[MyRow]
   * import MetaInstances._
   */

  import InstancesUuidMeta._

  case class MyRow(id: UUID)

  /** Meta combines Get & Put */

  val program: ConnectionIO[List[MyRow]] = sql"select id from uuids"
    .query[MyRow]
    .to[List]

  val io: IO[List[MyRow]] = program.transact(xa[IO])

  override def run: IO[Unit] =
    program
      .transact(xa[IO])
      .flatMap(_.traverse_(x => IO(pprint.pprintln(x))))

}
