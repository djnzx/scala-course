package degoes.fp_crawler

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Try}

object RecursionWithState_v2 extends App {

  case class CrawlState(visited: List[String], errors: List[String]) {
    def addVisited(value: String): CrawlState = copy(visited = visited :+ value)
    def logError(err: String): CrawlState = copy(errors = errors :+ err)
  }

  def iteration(line: String, state: CrawlState): CrawlState = Try(line.toInt) match {
    case Success(_) => state.addVisited(line)
    case Failure(_) => state.logError(line)
  }

  @tailrec
  def loop(state: CrawlState): CrawlState = {
    val line = readLine()
    if (line == "q") return state
    val state_new = iteration(line, state)
    loop(state_new)
  }

  val state_initial = CrawlState(List.empty[String], List.empty[String])
  val state: CrawlState = loop(state_initial)
  println(state.visited)
  println(state.errors)
}
