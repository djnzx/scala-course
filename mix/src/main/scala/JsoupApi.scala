package hr_parse

import org.jsoup.select.Elements

object JsoupApi {

  def fromHtml(el: Elements) = {
    val hacker = el.select(".hacker a").attr("data-value")
    val rank = el.select(".rank div").text
    val country = el.select(".extra .flag-tooltip").attr("data-balloon")
    val score = el.select(".score div").text
  }

}
