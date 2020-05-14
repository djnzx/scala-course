package hr_parse.db

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import io.getquill.MappedEncoding

case class v_ranks_current(sid: Int, sname: String,
                           gid: Int, gname: String,
                           hacker: String,
                           rank: Option[Int], score: Option[Float],
                           date: Option[ZonedDateTime]
                          )

object v_ranks_current {
  implicit val encZdt: MappedEncoding[ZonedDateTime, Date] =
    MappedEncoding { z => Date.from(z.toInstant) }
  implicit val decZdt: MappedEncoding[Date, ZonedDateTime] =
    MappedEncoding { d => d.toInstant.atZone(ZoneOffset.UTC) }
}

