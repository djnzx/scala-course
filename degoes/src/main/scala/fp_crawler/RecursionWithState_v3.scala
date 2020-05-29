package degoes.fp_crawler

import scala.util.{Failure, Success, Try}

object RecursionWithState_v3 extends App {

  case class CrawlState(visited: List[String], errors: List[String]) {
    def addVisited(value: String): CrawlState = copy(visited = visited :+ value)
    def logError(err: String): CrawlState = copy(errors = errors :+ err)
  }

  def iteration(line: String, state: CrawlState): CrawlState = Try(line.toInt) match {
    case Success(_) => state.addVisited(line)
    case Failure(_) => state.logError(line)
  }


  val state_initial = CrawlState(List.empty[String], List.empty[String])
//  val state: CrawlState = loop(List(state_initial)).head
//  println(state.visited)
//  println(state.errors)
}
