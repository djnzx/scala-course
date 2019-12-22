package _degoes.fp_crawler

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

object RecursionWithStateApp extends App {

  case class CrawlState(visited: List[String], errors: List[String]) {
    def addVisited(value: String): CrawlState = copy(visited = visited :+ value)
    def logError(err: String): CrawlState = copy(errors = errors :+ err)
  }

  @tailrec
  def action(line: String, state: CrawlState): CrawlState = {
    if (line == "q") return state

    val line2 = readLine
    val new_state = Try(line2.toInt) match {
      case Success(_) => state.addVisited(line2)
      case Failure(_) => state.logError(line2)
    }

    action(line2, new_state)
  }

  val state_initial = CrawlState(List.empty[String], List.empty[String])
  val state: CrawlState = action("start", state_initial)
  println(state.visited)
  println(state.errors)
}
