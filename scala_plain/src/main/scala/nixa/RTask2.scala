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
    val parsed: Array[Command] = commands.map(parse)

    type ST = immutable.Map[Int, Int]

    def process(map: (ST, Int), cmd: Command): (ST, Int) = (map, cmd) match {
      case (     (map, _), Create(tk, time))  => (map + (tk -> (time + exLim)), time)
      case (mm @ (map, _), Refresh(tk, time)) =>
        map.get(tk) match {
          case Some(exp) =>
            if (time <= exp) (map + (tk -> (time + exLim)), time)
            else             (map - tk,                     time)
          case _ => mm
        }
    }

    parsed.foldLeft((immutable.Map.empty[Int, Int], 0))(process) match {
      case (map, exp) => map.count { case (_, tm) => tm >= exp }
    }



  }

}
