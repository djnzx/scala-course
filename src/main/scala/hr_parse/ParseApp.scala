package hr_parse

import org.http4s.Uri
import org.jsoup._
import org.http4s.implicits._
import Data.users
import org.jsoup.nodes.Document

/**
  * Parser for HackerRank scores
  * https://www.hackerrank.com/leaderboard?filter=elgun_cumayev&filter_on=hacker&page=1&track=algorithms&type=practice
  */
object ParseApp extends App {

  val uriToDocument = (uri: Uri) =>
    Jsoup.connect(uri renderString).get

  val documentToElement = (doc: Document) =>
    doc.select(".ui-leaderboard-table .table-body .table-row")

  val buildUri = (topic: String) => (name: String) =>
    uri"https://www.hackerrank.com" / "leaderboard" withQueryParams Map(
      "filter" -> name,
      "filter_on" -> "hacker",
      "track" -> topic,
      "type" -> "practice"
    )

  val topic = "algorithms"

  val rqWithTopicByUser = buildUri(topic)

  users
    .map { rqWithTopicByUser }
    .map { uriToDocument }
    .map { documentToElement }
    .map { HackerDetails.fromHtml }
    .sortBy { _.rank }
    .foreach { println }
}
