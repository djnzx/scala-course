package hr_parse

import org.jsoup.select.Elements

case class HackerDetails(name: String, rank: Int, country: String, score: Double)

object HackerDetails {

  // TODO put validation here and make return type Either[String, HackerDetails]
  // No user, conversion error...
  def fromHtml(el: Elements): HackerDetails = {
    val hacker  = el.select(".hacker a").attr("data-value")
    val rank    = el.select(".rank div").text
    val country = el.select(".extra .flag-tooltip").attr("data-balloon")
    val score   = el.select(".score div").text
    HackerDetails(hacker, rank.toInt, country, score.toDouble)
  }

}

