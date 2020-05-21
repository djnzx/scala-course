package hr_parse.db

import java.time.ZonedDateTime

import hr_parse.Flow._
import hr_parse.HackerDetails
import hr_parse.db.tables.students

object AddCurrentRank extends App {
  val q = new QuillConn
  import q.ctx
  import q.ctx._

  val logins = quote {
    query[students].filter(_.hacker.isDefined).map(s => (s.id, s.hacker.getOrElse("")))
  }
  val ls: List[(Index, String)] = ctx.run(logins)

  case class ranks(id: Int, date: ZonedDateTime, student: Int, rank: Int, score: Double)

  val dataToInsert: List[(q.ctx.Index, HackerDetails)] = ls
    .map { case (id, login) => (id, userToUri(login)) }
    .map { case (id, uri) => (id, uriToDocument(uri)) }
    .map { case (id, doc) => (id, documentToElements(doc)) }
    .map { case (id, elm) => (id, HackerDetails.fromHtml(elm)) }

  val insert = quote {
    liftQuery(dataToInsert).foreach { case (id, hd) => query[ranks].insert(
      _.student -> id,
      _.rank    -> hd.rank,
      _.score   -> hd.score
    )}
  }
  ctx.run(insert)
}
