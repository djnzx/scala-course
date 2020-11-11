package diwo

object EuroMillions extends App {
  implicit class ExSyntax(s: String) {
    def unary_! = throw new IllegalArgumentException(s)
  }
  implicit class RichEither[L,R](e: Either[L,R]) {
    def mapLeft[L2](f: L => L2): Either[L2, R] = e match {
      case Right(r) => Right(r)
      case Left(l)  => Left(f(l)) 
    }
  }
  
  val NC = 5   //     5 of 50
  val NCS = 10 // 5..10 of 50
  val SNC = 2  //     2 of 11
  val SNCS = 5 //  2..5 of 11
  case class Draw(ns: Set[Int], sns: Set[Int])
  trait Ticket {
    def ns: Set[Int]
    def sns: Set[Int]
  }
  
  def validate(s: Set[Int])(p: Set[Int] => Boolean) = 
    Some(s).filter(p)
  
  def sizeEq(s: Set[Int], sz: Int) = 
    validate(s)(_.size == sz)
      .toRight(mustEq(sz, s.size))

  def sizeBtw(s: Set[Int], mn: Int, mx: Int) =
    validate(s)(s => s.size >= mn && s.size <= mx)
      .toRight(mustBtw(mn, mx, s.size))

  def mustEq(must: Int, given: Int) = s"must equal $must, $given given"
  def mustBtw(mn: Int, mx: Int, given: Int) = s"must be in range[$mn, $mx], $given given"
  def cn(s: String) = s"count of numbers $s"
  def csn(s: String) = s"count of star numbers $s"
  def nt(s: String) = s"Normal ticket $s"
  def st(s: String) = s"System ticket $s"
  
  /** 5/50 + 2/11 */
  case class NormalTicket private(ns: Set[Int], sns: Set[Int]) extends Ticket
  object NormalTicket {
    def apply(ns: Set[Int], sns: Set[Int]) =
      (for {
        nor <- sizeEq(ns, NC).mapLeft(cn)
        str <- sizeEq(sns, SNC).mapLeft(csn)
      } yield (nor, str))
        .mapLeft(nt)
        .map { case (ns, sns) => new NormalTicket(ns, sns) }
        .fold(!_, identity)
  }
  /** up to 10/50 + up to 5/11 */
  case class SystemTicket private (ns: Set[Int], sns: Set[Int]) extends Ticket
  object SystemTicket {
    def apply(ns: Set[Int], sns: Set[Int]) =
      (for {
        nor <- sizeBtw(ns, NC, NCS).mapLeft(cn)
        str <- sizeBtw(sns, SNC, SNCS).mapLeft(csn)
      } yield (nor, str))
        .mapLeft(st)
        .map { case (ns, sns) => new SystemTicket(ns, sns) }
        .fold(!_, identity)
  }
  
  def prize(t: Ticket, d: Draw): Option[Int] =
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
      case (2, 0) => Some(10)
      case (1, 2) => Some(11)
      case (2, 1) => Some(12)
      case (2, 0) => Some(13)
      case _      => None
    }
  
  /** 
    *               n!
    * C(n, k) = -----------
    *           k! * (n-k)!
    *
    * C(10,5) = 252
    * C(5,2) = 10
    *
    * total up to 252 * 10 = 2520
    */
  private def tails[A](la: Seq[A])(f: Seq[A] => Seq[Seq[A]]): Seq[Seq[A]] = la match {
    case Nil => Seq.empty
    case _ :: t => f(la) ++ tails(t)(f)
  }

  def allCombN[A](n: Int, as: Seq[A]): Seq[Seq[A]] = n match {
    case 0 => Seq(Seq.empty)
    case _ => tails(as) { case h :: t => allCombN(n - 1, t).map(h +: _) }
  }

  def allCombinations(t: SystemTicket) = for {
    n  <- allCombN(NC, t.ns.toSeq)
    sn <- allCombN(SNC, t.sns.toSeq)
  } yield NormalTicket(n.toSet, sn.toSet)
  
  def normalize(t: Ticket) = t match {
    case nt: NormalTicket => Seq(nt) 
    case st: SystemTicket => allCombinations(st)
  }

  def calculateResults1(d: Draw, ts: Seq[Ticket]) =
    ts.flatMap(normalize)
      .flatMap(prize(_, d))
      .groupMapReduce(identity)(_ => 1)(_ + _)

  def calculateResults2(d: Draw, ts: Seq[Ticket]) =
    ts.flatMap(prize(_, d))
      .groupMapReduce(identity)(_ => 1)(_ + _)

  case class WinningClass(n: Int) {
    override def toString: String = s"Winning class $n"
  }

  def represent(outcome: Map[Int, Int]) =
    outcome
      .toVector
      .map { case (t, n) => WinningClass(t) -> n }
      .map { case (m, n) => s"$m : $n" }

}
