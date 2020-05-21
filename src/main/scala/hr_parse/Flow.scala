package hr_parse

import org.http4s.Uri
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.http4s.implicits._
import org.jsoup.select.Elements

object Flow {
  // TODO: change return types to handle exceptions
  val uriToDocument: Uri => Document = (uri: Uri) =>
    Jsoup.connect(uri renderString).get

  val documentToElements: Document => Elements = (doc: Document) =>
    doc.select(".ui-leaderboard-table .table-body .table-row")

  val buildUri: String => String => Uri = (topic: String) => (name: String) =>
    uri"https://www.hackerrank.com" / "leaderboard" withQueryParams Map(
      "filter"    -> name,
      "filter_on" -> "hacker",
      "track"    -> topic,
      "type"     -> "practice"
    )

  val topic = "algorithms"

  val userToUri: String => Uri = buildUri(topic)

  // TODO: consider fetching documents in parallel in another thread pool

}
