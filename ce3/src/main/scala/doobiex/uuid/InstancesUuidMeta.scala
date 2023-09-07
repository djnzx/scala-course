package doobiex.uuid

import doobie.Meta
import java.util.UUID

object InstancesUuidMeta {

  /** the rule how to Map Scala types to Postgres
    *
    * `Meta[A] = Get[A] + Put[A]`
    *
    * actually, just a map + contramap combination.
    * In case of `UUID` we describe two functions:
    *
    *  - `String => UUID`
    *  - `UUID => String`
    */
  implicit val uuidMeta: Meta[UUID] =
    Meta[String].imap { raw: String =>
      UUID.fromString(raw)
    } { uuid: UUID =>
      uuid.toString
    }

}
