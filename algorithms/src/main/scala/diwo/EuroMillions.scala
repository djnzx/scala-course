package diwo

import scala.util.{Try, Using}

object EuroMillions extends App {
  /** syntax to throw an exception */
  implicit class ExSyntax(s: String) {
    def unary_! = throw new IllegalArgumentException(s)
  }
  /** missed stuff on Either */
  implicit class RichEither[L, R](e: Either[L, R]) {
    def mapLeft[L2](f: L => L2): Either[L2, R] = e match {
      case Right(r) => Right(r)
      case Left(l)  => Left(f(l)) 
    }
  }
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
  /** normal ticket 5/50 + 2/11 */
  case class NormalTicket private(ns: Set[Int], sns: Set[Int]) extends Ticket
  /** system ticket: up to 10/50 + up to 5/11 */
  case class SystemTicket private(ns: Set[Int], sns: Set[Int]) extends Ticket
  /** draw: 5/50 + 2/11*/
  case class Draw private(ns: Set[Int], sns: Set[Int])
  /** validation messages */
  def msg_mustEq(must: Int, given: Int) = s"must equal $must, $given given"
  def msg_mustBtw(mn: Int, mx: Int, given: Int) = s"must be in range[$mn, $mx], $given given"
  def msg_cn(s: String) = s"count of numbers $s"
  def msg_csn(s: String) = s"count of star numbers $s"
  def msg_nt(s: String) = s"Normal ticket $s"
  def msg_st(s: String) = s"System ticket $s"
  def msg_dr(s: String) = s"Draw $s"
  /** generic validator */
  def validate(s: Set[Int])(p: Set[Int] => Boolean) = Some(s).filter(p)
  /** exact size validation */
  def sizeEq(s: Set[Int], sz: Int) = 
    validate(s)(_.size == sz)
      .toRight(msg_mustEq(sz, s.size))
  /** range validation */
  def sizeBtw(s: Set[Int], mn: Int, mx: Int) = 
    validate(s)(s => s.size >= mn && s.size <= mx)
      .toRight(msg_mustBtw(mn, mx, s.size))
  /** normal validation: shared in normal ticket and draw */
  def normalValidation(ns: Set[Int], sns: Set[Int]) = for {
    nor <- sizeEq(ns, NC).mapLeft(msg_cn)
    str <- sizeEq(sns, SNC).mapLeft(msg_csn)
  } yield (nor, str)
  /** helper stuff to implement validation */
  def toInt(s: String) = Try(s.toInt).toOption
  def parse2arrays(s: String) =
    Option(s)
      .map(_.trim)
      .map(_.split("/"))
      .filter(_.length == 2)
      .map(_.map(_.split(",")))
      .map(_.map(_.map(_.trim)))
      .map(_.map(_.flatMap(toInt)))
      .map { case Array(a, b) => a.toSet -> b.toSet }
  /** attaching validation to NormalTicket syntax */
  object NormalTicket {
    def apply(ns: Set[Int], sns: Set[Int]) =
      normalValidation(ns, sns)
        .map { case (ns, sns) => new NormalTicket(ns, sns) }
        .mapLeft(msg_nt)
    def buildOrDie(ns: Set[Int], sns: Set[Int]) =
      apply(ns, sns)
        .fold(!_, identity)
  }
  /** attaching validation to SystemTicket syntax */
  object SystemTicket {
    def apply(ns: Set[Int], sns: Set[Int]) =
      (for {
        nor <- sizeBtw(ns, NC, NCS).mapLeft(msg_cn)
        str <- sizeBtw(sns, SNC, SNCS).mapLeft(msg_csn)
      } yield (nor, str))
        .map { case (ns, sns) => new SystemTicket(ns, sns) }
        .mapLeft(msg_st)
  }
  /** attaching validation to Draw syntax */    
  object Draw {
    def parse(s: String) =
      parse2arrays(s)
        .toRight(!s"error parsing Draw, $s given")
        .flatMap { case (a, b) => apply(a, b) }
    def apply(ns: Set[Int], sns: Set[Int]) =
      normalValidation(ns, sns)
        .map { case (ns, sns) => new Draw(ns, sns) }
        .mapLeft(msg_dr)
  }
  /** analyzing different tickets */
  object Ticket {
    def apply(s: String) =
      parse2arrays(s)
        .flatMap { case (a, b) =>
          /** nested stuff to avoid double checking in tuple*/
          NormalTicket(a, b) match {
            case Right(nt)   => Some(nt)  // normal
            case _ => SystemTicket(a, b) match {
              case Right(st) => Some(st)  // system 
              case _         => None      // wrong
            }
          }
        }
  }
  /** calculating prize */
  def prize(d: Draw, t: Ticket): Option[Int] =
    (t.ns.intersect(d.ns).size, t.sns.intersect(d.sns).size) match {
      case (5, 2) => Some(1)
      case (5, 1) => Some(2)
      case (5, 0) => Some(3)
      case (4, 2) => Some(4)
      case (4, 1) => Some(5)
      case (4, 0) => Some(6)
      case (3, 2) => Some(7)
      case (2, 2) => Some(8)
      case (3, 1) => Some(9)
      case (3, 0) => Some(10)
      case (1, 2) => Some(11)
      case (2, 1) => Some(12)
      case (2, 0) => Some(13)
      case _      => None
    }
  /**
    * Combinatorics and math stuff:
    * {{{
    *               n!
    * C(n, k) = -----------
    *           k! * (n-k)!
    *
    * C(10,5) = 252
    * C(5,2) = 10
    * }}}
    * total up to 252 * 10 = 2520
    */
  private def tails[A](la: Seq[A])(f: Seq[A] => Seq[Seq[A]]): Seq[Seq[A]] = la match {
    case Nil => Seq.empty
    case _ :: t => f(la) ++ tails(t)(f)
  }
  /** generic combinations */
  def allCombN[A](n: Int, as: Seq[A]): Seq[Seq[A]] = n match {
    case 0 => Seq(Seq.empty)
    case _ => tails(as) { case h :: t => allCombN(n - 1, t).map(h +: _) }
  }
  /** combinations enriched with business logic */
  def allCombinations(t: SystemTicket) = for {
    n  <- allCombN(NC, t.ns.toSeq)
    sn <- allCombN(SNC, t.sns.toSeq)
  } yield NormalTicket.buildOrDie(n.toSet, sn.toSet)
  /** expand SystemTicket to Seq[NormalTicket] */
  def expand(t: SystemTicket) = allCombinations(t)
  /** normalize any ticket to Seq[NormalTicket] */
  def normalize(t: Ticket) = t match {
    case nt: NormalTicket => Seq(nt) 
    case st: SystemTicket => expand(st)
  }
  /** normalize, flatten, apply `prize`, and group results */
  def calculateResults(d: Draw, ts: Seq[Ticket]) =
    ts.flatMap(normalize)
      .flatMap(prize(d, _))
      .groupMapReduce(identity)(_ => 1)(_ + _)
  /** WinningClass representation */
  case class WinningClass(n: Int) {
    override def toString: String = s"Winning class $n"
  }
  /** result line representation */
  case class ResultLine(wc: WinningClass, count: Int) {
    override def toString: String = s"$wc : $count"
  }
  /** represent results */
  def represent(outcome: Map[Int, Int]) =
    outcome
      .toVector
      .sortBy(_._1)
      .map { case (t, n) => WinningClass(t) -> n }
      .map((ResultLine.apply _).tupled)
  /** obtain file from resources folder */
  def obtainResource(fileName: String) =
    Option(getClass.getClassLoader.getResource(fileName))
      .map(_.getFile)
  /** obtain file, or die with meaningful message */
  def resourceOrDie(fileName: String) =
    obtainResource(fileName)
      .getOrElse(!s"file $fileName not found")
  /** main process */
  def process(draw: String, tickets: String) = 
    Using.resources(
      scala.io.Source.fromFile(resourceOrDie(draw)),
      scala.io.Source.fromFile(resourceOrDie(tickets))
    ) { case (draw, tickets) =>
      draw.getLines()
        .nextOption()
        .toRight(s"file $draw is empty")
        .flatMap(Draw.parse)
        .foreach { d: Draw =>
        val ts = tickets.getLines().flatMap(Ticket(_)).toSeq
        val out = calculateResults(d, ts)
        represent(out)
          .foreach(println)
      }
    }

  process("draw.txt", "tickets.txt")
}
