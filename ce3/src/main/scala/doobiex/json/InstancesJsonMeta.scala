package doobiex.json

import cats.implicits._
import doobie._
import io.circe.Json
import org.postgresql.util.PGobject

object InstancesJsonMeta {

  implicit val JsonbMeta: Meta[Json] = Meta.Advanced
    .other[PGobject]("jsonb")
    .timap[Json] { pgo: PGobject =>
      io.circe.parser
        .parse(pgo.getValue)
        .leftMap[Json](err => throw err)
        .merge
    } { json: Json =>
      val o = new PGobject
      o.setType("jsonb")
      o.setValue(json.noSpaces)
      o
    }

}
