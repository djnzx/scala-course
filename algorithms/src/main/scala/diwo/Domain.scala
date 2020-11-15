package diwo

import diwo.valid.{normalValidation, parse2arrays, sizeBtw}
import ExtendedSyntax.RichEither

object Domain {

  /** constants to avoid magic number in code */
  val NC = 5   //     5 of 50
  val SNC = 2  //     2 of 11
  val NCS = 10 // 5..10 of 50
  val SNCS = 5 //  2..5 of 11
  
  /** base trait for ticket */
  trait Ticket {
    def ns: Set[Int]
    def sns: Set[Int]
  }
  
  /** normal ticket: 5/50 + 2/11 */
  case class NormalTicket private(ns: Set[Int], sns: Set[Int]) extends Ticket
  
  /** system ticket: up to 10/50 + up to 5/11 */
  case class SystemTicket private(ns: Set[Int], sns: Set[Int]) extends Ticket
  
  /** draw: 5/50 + 2/11 */
  case class Draw private(ns: Set[Int], sns: Set[Int])
  
  /** attaching validation to NormalTicket syntax */
  object NormalTicket {
    def apply(ns: Set[Int], sns: Set[Int]) =
      normalValidation(ns, sns)
        .map { case (ns, sns) => new NormalTicket(ns, sns) }
        .mapLeft(msg.nt)
    def buildOrDie(ns: Set[Int], sns: Set[Int]) =
      apply(ns, sns)
        .getOrDie
  }
  
  /** attaching validation to SystemTicket syntax */
  object SystemTicket {
    def apply(ns: Set[Int], sns: Set[Int]) =
      (for {
        nor <- sizeBtw(ns, NC, NCS).mapLeft(msg.cn)
        str <- sizeBtw(sns, SNC, SNCS).mapLeft(msg.csn)
      } yield (nor, str))
        .map { case (ns, sns) => new SystemTicket(ns, sns) }
        .mapLeft(msg.st)
  }
  
  /** attaching validation to Draw syntax */
  object Draw {
    def parse(s: String) =
      parse2arrays(s)
        .toRight(msg.dr_parse(s))
        .flatMap { case (a, b) => apply(a, b) }
    def apply(ns: Set[Int], sns: Set[Int]) =
      normalValidation(ns, sns)
        .map { case (ns, sns) => new Draw(ns, sns) }
        .mapLeft(msg.dr)
  }
  
  /** analyzing different tickets */
  object Ticket {
    def apply(s: String) =
      parse2arrays(s)
        .flatMap { case (a, b) =>
          NormalTicket(a, b)
            .or(SystemTicket(a, b))
            .toOption
        }
  }

}
