package diwo

import diwo.Domain.{Draw, Ticket}

object Results {
  
  /** WinningClass representation */
  case class WinningClass(n: Int) {
    override def toString: String = s"Winning class $n"
  }
  
  /** result line representation */
  case class ResultLine(wc: WinningClass, count: Int) {
    override def toString: String = s"$wc : $count"
  }
  
  /** normalize, flatten, apply `prize`, and group results */
  def calculate(d: Draw, ts: Seq[Ticket]) =
    ts.flatMap(Norm.normalize)
      .flatMap(Prize.calculate(d, _))
      .groupMapReduce(identity)(_ => 1)(_ + _)
      
  /** represent results */
  def represent(outcome: Map[Int, Int]) =
    outcome
      .toVector
      .sortBy(_._1)
      .map { case (t, n) => WinningClass(t) -> n }
      .map((ResultLine.apply _).tupled)

}
