package hr_parse

import hr_parse.Data.users
import hr_parse.Flow._

/**
  * Parser for HackerRank scores
  * https://www.hackerrank.com/leaderboard?filter=elgun_cumayev&filter_on=hacker&page=1&track=algorithms&type=practice
  */
object ParseApp extends App {

  users
    .map { userToUri }
    .map { uriToDocument }
    .map { documentToElements }
    .map { HackerDetails.fromHtml }
    .sortBy { _.rank }
    .foreach { println }
}
