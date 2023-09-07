package doobiex.json

import cats.effect._
import cats.implicits._
import doobie.implicits._
import doobiex.xa
import io.circe.Json
import java.util.UUID
import doobiex.uuid.InstancesUuidMeta

object JsonMappingApp extends IOApp.Simple {

  import InstancesUuidMeta._
  import InstancesJsonMeta._
  case class Row(id: UUID, value: Option[Json])

  val program = sql"select id, value from jsons"
    .query[Row]
    .to[List]

  override def run: IO[Unit] =
    program
      .transact(xa[IO])
      .flatMap(x => x.traverse_(x =>  IO(pprint.pprintln(x))))

}
