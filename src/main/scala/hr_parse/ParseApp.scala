package hr_parse

import org.http4s.Uri
import org.jsoup._
import org.jsoup.select.Elements
import org.http4s.implicits._

/**
  * Parser for HackerRank scores
  * https://www.hackerrank.com/leaderboard?filter=elgun_cumayev&filter_on=hacker&page=1&track=algorithms&type=practice
  */
object ParseApp extends App {

  case class HackerDetails(name: String, rank: Int, country: String, score: Double)

  // TODO put validation here and make return type Either[String, HackerDetails]
  val toHacker = (el: Elements) => {
    val hacker  = el.select(".hacker a").attr("data-value")
    val rank    = el.select(".rank div").text
    val country = el.select(".extra .flag-tooltip").attr("data-balloon")
    val score   = el.select(".score div").text
    HackerDetails(hacker, rank.toInt, country, score.toDouble)
  }

  val toElement = (uri: Uri) =>
    Jsoup.connect(uri renderString).get
      .select(".ui-leaderboard-table .table-body .table-row")

  val buildUri = (topic: String) => (name: String) =>
    uri"https://www.hackerrank.com" / "leaderboard" withQueryParams Map(
      "filter" -> name,
      "filter_on" -> "hacker",
      "track" -> topic,
      "type" -> "practice"
    )

  val topic = "algorithms"
  val names = Vector("elgun_cumayev", "alexr007", "ibcelal", "lcavadova", "realserxanbeyli")

  val rqWithTopic = buildUri(topic)

  names
    .map { rqWithTopic }
    .map { toElement }
    .map { toHacker }
    .sortBy { _.rank }
    .foreach { println }
}
