package hr_parse.db.tables

import java.time.ZonedDateTime

case class v_ranks_current(sid: Int, sname: String,
                           gid: Int, gname: String,
                           hacker: String,
                           rank: Option[Int], score: Option[Float],
                           date: Option[ZonedDateTime]
                          )

