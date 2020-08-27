package nixa

import scala.collection.immutable

object TaskR2 extends App {

  def numberOfTokens(expLim: Int, commands: Array[Array[Int]]): Int = {
    /** Domain representation */
    sealed trait Command
    case class Create(tk: Int, time: Int) extends Command
    case class Refresh(tk: Int, time: Int) extends Command
    object Command {
      def parse(cmd: Array[Int]): Command = cmd match {
        case Array(0, tk, tm) => Create(tk, tm)
        case Array(1, tk, tm) => Refresh(tk, tm)
      }
    }
    /** State representation */
    type MII = immutable.Map[Int, Int]
    case class ST(map: MII, max: Int) {
      private def withMax(mx: Int): ST = copy(max = mx)
      private def withMap(mp: MII): ST = copy(map = mp)

      def update(token: Int, time: Int): ST = 
        withMap(map + (token -> (time + expLim))).withMax(time)
      def refresh(token: Int, time: Int): ST =
        map.get(token)
          .map(
            Option(_)
            .filter(time <= _)
            .map(_ => update(token, time))
            .getOrElse(withMap(map - token).withMax(time))
          ).getOrElse(withMax(time))
    }
    /** fold initial value */
    val zero = ST(immutable.Map.empty, 0)
    /** fold function */
    def process(mm: ST, cmd: Command): ST = cmd match {
      case Create (tk, time) => mm.update(tk, time)
      case Refresh(tk, time) => mm.refresh(tk, time)
    }
    /** The task */
    commands
      .map(Command.parse)
      .foldLeft(zero)(process) match {
      case ST(map, last) => map.count { case (_, exp) => exp >= last }
    }

  }

}
