package doobiex.json

import cats.Show
import cats.data.NonEmptyList
import cats.implicits._
import doobie._
import io.circe._
import org.postgresql.util.PGobject

object InstancesJsonGet {

  implicit val showPGobject: Show[PGobject] = Show.show(_.getValue)

  implicit val jsonGet: Get[Json] =
    Get.Advanced
      .other[PGobject](NonEmptyList.of("json"))
      .temap[Json] { o: PGobject =>
        val parsed: Either[ParsingFailure, Json] = io.circe.parser.parse(o.getValue)
        val remapped: Either[String, Json] = parsed.leftMap(_.show)
        remapped
      }

}
