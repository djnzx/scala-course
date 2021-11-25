package diwo

import diwo.Combinatorics.allCombN
import diwo.Domain.NC
import diwo.Domain.NormalTicket
import diwo.Domain.SNC
import diwo.Domain.SystemTicket
import diwo.Domain.Ticket

object Norm {

  /** combinations enriched with business logic */
  def allCombinations(t: SystemTicket) = for {
    n <- allCombN(NC, t.ns.toList)
    sn <- allCombN(SNC, t.sns.toList)
  } yield NormalTicket.buildOrDie(n.toSet, sn.toSet)

  /** expand SystemTicket to Seq[NormalTicket] */
  def expand(t: SystemTicket) = allCombinations(t)

  /** normalize any ticket to Seq[NormalTicket] */
  def normalize(t: Ticket) = t match {
    case nt: NormalTicket => Seq(nt)
    case st: SystemTicket => expand(st)
  }

}
