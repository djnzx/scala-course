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
    
    case class ST(map: immutable.Map[Int, Int], max: Int) {
      def upd(tk: Int, time: Int): ST = ST(map + (tk -> (time + exLim)), time)
      def max(         time: Int): ST = copy(                      max = time)
      def del(tk: Int, time: Int): ST = ST(map - tk,                     time) 
    }
    
    val zero = ST(immutable.Map.empty, 0)
    
    def process(mm: ST, cmd: Command): ST = (mm, cmd) match {
      case (ST(map, _), Create (tk, time)) =>
                           mm.upd(tk, time)
      case (ST(map, _), Refresh(tk, time)) => map.get(tk) match {
        case Some(exp) =>
          if (time <= exp) mm.upd(tk, time)
          else             mm.del(tk, time)
        case _ =>          mm.max(time)                   
      }
    }

    commands
      .map(parse)
      .foldLeft(zero)(process) match {
      case ST(map, last) => map.count { case (_, exp) => exp >= last }
    }
    
  }

}
