package nixa

import scala.collection.immutable

object TaskR2 extends App {

  def numberOfTokens(expLim: Int, commands: Array[Array[Int]]): Int = {
    sealed trait Command
    case class Create(tk: Int, time: Int) extends Command
    case class Refresh(tk: Int, time: Int) extends Command
    object Command {
      def apply(cmd: Array[Int]): Command = cmd match {
        case Array(0, tk, tm) => Create(tk, tm)
        case Array(1, tk, tm) => Refresh(tk, tm)
      }
    }
    type MII = immutable.Map[Int, Int]
    case class ST(map: MII, max: Int) {
      private def withMax(mx: Int): ST = copy(max = mx)
      private def withMap(mp: MII): ST = copy(map = mp)
      
      def update(tk: Int, newMax: Int): ST = withMap(map + (tk -> (newMax + expLim))).withMax(newMax)  
      def refresh(tk: Int, newMax: Int): ST =
        map.get(tk)
          .map(Option(_)
            .filter(newMax <= _)
            .map(_ => update(tk, newMax))
            .getOrElse(withMap(map - tk).withMax(newMax))
          ).getOrElse(withMax(newMax))
//      map.get(tk) match {
//        case Some(exp) =>
//          if (newMax <= exp) update(tk, newMax)
//          else               withMap(map - tk).withMax(newMax)
//        case _ =>            withMax(newMax)
//      }
    }
    
    val zero = ST(immutable.Map.empty, 0)
    
    def process(mm: ST, cmd: Command): ST = cmd match {
      case Create (tk, time) => mm.update(tk, time)
      case Refresh(tk, time) => mm.refresh(tk, time)  
    }

    commands
      .map(Command(_))
      .foldLeft(zero)(process) match {
      case ST(map, last) => map.count { case (_, exp) => exp >= last }
    }
    
  }

}
