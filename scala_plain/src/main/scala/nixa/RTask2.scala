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
    def process(map: (ST, Int), cmd: Command): (ST, Int) = cmd match {
      case Create(tk, time) => (map._1 + (tk -> (time + exLim)), time)
        
      case Refresh(tk, time) => 
        map._1.contains(tk) match {
        case false => (map._1, time)
        case true  =>
          if (time <= map._1(tk)) (map._1 + (tk -> (time + exLim)), time)
          else                    (map._1 - tk,                     time)
      }
    }
    
    parsed.foldLeft((immutable.Map.empty[Int, Int], 0))(process) match {
      case (map, exp) => map.count { case (_, tm) => tm >= exp }
    }
    
    
    
  }

}
