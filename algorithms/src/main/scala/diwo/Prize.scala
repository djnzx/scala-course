package diwo

import diwo.Domain.{Draw, Ticket}

object Prize {
  
  /** prizes description */
  val prizes = Seq(
    (5, 2) -> 1,
    (5, 1) -> 2,
    (5, 0) -> 3,
    (4, 2) -> 4,
    (4, 1) -> 5,
    (4, 0) -> 6,
    (3, 2) -> 7,
    (2, 2) -> 8,
    (3, 1) -> 9,
    (3, 0) -> 10,
    (1, 2) -> 11,
    (2, 1) -> 12,
    (2, 0) -> 13,
  )

  /** calculating prize */
  def calculate(d: Draw, t: Ticket) =
    ((t.ns & d.ns).size, (t.sns & d.sns).size) match {
      case iss => prizes.collectFirst { case (`iss`, x) => x }
    }

}
