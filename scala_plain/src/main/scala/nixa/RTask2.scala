package nixa

import scala.collection.immutable

object RTask2 extends App {

  def numberOfTokens(exLim: Int, commands: Array[Array[Int]]): Int = {
    sealed trait Command
    case class Create(tk: Int, time: Int) extends Command
    case class Refresh(tk: Int, time: Int) extends Command
    def parse(cmd: Array[Int]): Command = cmd match {
      case Array(0, tk, tm) => Create(tk, tm)
      case Array(1, tk, tm) => Refresh(tk, tm)
    }

    type ST = (immutable.Map[Int, Int], Int)
    val zero: ST = (immutable.Map.empty[Int, Int], 0)
    
    def process(mm: ST, cmd: Command): ST = (mm, cmd) match {
      case ((map, _), Create (tk, time)) =>
                           (map + (tk -> (time + exLim)), time)
      case ((map, _), Refresh(tk, time)) => map.get(tk) match {
        case Some(exp) =>
          if (time <= exp) (map + (tk -> (time + exLim)), time)
          else             (map - tk,                     time)
        case _ =>          (map,                          time)
      }
    }

    commands
      .map(parse)
      .foldLeft(zero)(process) match {
      case (map, last) => map.count { case (_, exp) => exp >= last }
    }
    
  }

}
