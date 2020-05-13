package hr_parse

import org.jsoup._
import org.jsoup.select.Elements
import org.http4s.implicits._

object ParseApp extends App {

  case class HackerDetails(name: String, rank: Int, country: String, score: Double)

  val elementToHacker = (el: Elements) => {
    val hacker = el.select(".hacker a").attr("data-value")
    val rank = el.select(".rank div").text
    val country = el.select(".extra .flag-tooltip").attr("data-balloon")
    val score = el.select(".score div").text
    HackerDetails(hacker, rank.toInt, country, score.toDouble)
  }

  val linkToElement = (link: String) => {
    val doc = Jsoup.connect(link).get
    doc.select(".ui-leaderboard-table .table-body .table-row")
  }

  val buildUri: (String, String) => String = (name: String, topic: String) =>
    uri"https://www.hackerrank.com" / "leaderboard" +?
      ("filter", name) +? ("filter_on", "hacker") +? ("track", topic) +? ("type", "practice") renderString

  /**
    * https://www.hackerrank.com/leaderboard?filter=elgun_cumayev&filter_on=hacker&page=1&track=algorithms&type=practice
    */
  val requestWithTopic = (user: String, topic: String) =>  elementToHacker(linkToElement(buildUri(user, topic)))

  val names = Vector("elgun_cumayev", "alexr007")
  val topic = "algorithms"

  val request = (user: String) => requestWithTopic(user, topic)

  val results: Vector[HackerDetails] = names.map(request)
  results foreach println
}
