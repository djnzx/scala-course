package degoes.fp_crawler

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

object RecursionWithState_v1 extends App {

  case class CrawlState(visited: List[String], errors: List[String]) {
    def addVisited(value: String): CrawlState = copy(visited = visited :+ value)
    def logError(err: String): CrawlState = copy(errors = errors :+ err)
  }

  @tailrec
  def action(line: String, state: CrawlState): CrawlState = {
    if (line == "q") return state

    val line_new = readLine
    val state_new = Try(line_new.toInt) match {
      case Success(_) => state.addVisited(line_new)
      case Failure(_) => state.logError(line_new)
    }

    action(line_new, state_new)
  }

  val state_initial = CrawlState(List.empty[String], List.empty[String])
  val state: CrawlState = action("start", state_initial)
  println(state.visited)
  println(state.errors)
}
