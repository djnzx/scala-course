package fp_red.red09.algebra

object MonoPolyNom {
  
  case class Monom(k: Int, p: Int) {
    def isNeg = k < 0
    def isZero = k == 0
    // with + or -
    override def toString: String = (isNeg, !isZero) match {
      case (true, _) => mkString
      case (_, true) => s"+$mkString"
      case _         => "" 
    }
    // with `-` only
    def mkString = {
      // sign
      val ss = if (isNeg) "-" else ""
      // abs(k)
      val absk = math.abs(k)
      // power
      val xs = p match {
        case 0 => ""
        case 1 => "x"
        case p => s"x^$p"
      }
      // eliminate k if ==1
      val ks = if (absk != 1) s"$absk" else ""
      if (!isZero) s"$ss$ks$xs" else ""
    }
  }

  object Monom {
    implicit class MonomOps(m1: Monom) {
      def unary_-  = m1.copy(k = - m1.k)
      def +(m2: Monom): Polynom = Polynom(Seq(m1, m2)).squash
      def *(m2: Monom): Monom = Monom(m1.k * m2.k, m1.p + m2.p)
      def /(m2: Monom): Monom = Monom(m1.k / m2.k, m1.p - m2.p)
      def /(den: Int): Monom = / (Monom(den, 0))
      def toPolynom = Polynom.of((m1.k, m1.p))
      def isNumber = m1.p == 0
    }
    // sort monoms in descending powers
    implicit val ordering: Ordering[Monom] = (x, y) => y.p - x.p
  }

  case class Polynom(ms: Seq[Monom]) {
    def sorted = Polynom(ms sorted)
    def isZero = ms == Nil
    def unary_- = Polynom(ms map { _.unary_- })
    /**
      * 1. squash the same powers
      * 2. remove with K=0
      * 3. sort
      */
    def squash = Polynom(
      // use P as a key
      // sum K and use as the value
      ms.groupMapReduce(_.p)(_.k)(_+_)
        .map { case (p, k) => Monom(k, p) }
        .filter { !_.isZero }
        .toVector
        .sorted
    )
    def +(p2: Polynom) = Polynom(ms ++ p2.ms).squash
    def -(p2: Polynom) = Polynom(ms ++ (-p2).ms).squash
    def *(p2: Polynom) = Polynom(for {
      m1 <- ms
      m2 <- p2.ms
    } yield m1 * m2).squash
    def /(den: Int): Polynom = Polynom(ms.map { _ / den }).squash
    def /(m: Monom): Polynom = Polynom(ms.map { _ / m }).squash
    def /(p2: Polynom): Polynom = p2.isMonom match {
      case true  => this / p2.toMonom
      case false => throw new RuntimeException("impossible to divide by polynom (not a monom)")
    }
    def isMonom = ms.length == 1
    def toMonom = ms.length match {
      case 1 => ms.head
      case _ => throw new RuntimeException("impossible to get monom from polynom")
    }
    def isNumber = isMonom && ms.head.p == 0
    def toNumber = isNumber match {
      case true => ms.head.k
      case _ => throw new RuntimeException("impossible to get number from not a monom")
    }
    override def toString: String = (ms.head.mkString ++ ms.tail.map { _.toString }) mkString ""
  }
  object Polynom {
    def of(mst: (Int, Int)*) = this(mst.map { case (k, p) => Monom(k, p) })
    def empty = of()
  }
}
