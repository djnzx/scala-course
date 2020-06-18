package fp_red.red09.algebra

object MonoPolyNom {
  case class Monom(k: Int, p: Int) {
    def isNeg = k < 0
    def isZero = k == 0
    def unary_- = copy(k = -this.k)
    def +(m2: Monom) = Polynom(Seq(this, m2)).squash
    def *(m: Monom) = Monom(k*m.k, p+m.p)
    def /(den: Int) = Monom(k/den, p)
    def toPolynom = Polynom.of((k, p))
    override def toString: String = if (isNeg) mkString else if (!isZero) s"+$mkString" else ""
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
    // sort monoms in descending powers
    implicit val ordering: Ordering[Monom] = (x, y) => y.p - x.p
  }

  case class Polynom(ms: Seq[Monom]) {
    def isZero = ms == Nil
    def unary_- = Polynom(ms map { _.unary_- })
    def sorted = Polynom(ms sorted)
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
    def *(p2: Polynom) = Polynom(for {
      m1 <- ms
      m2 <- p2.ms
    } yield m1 * m2).squash
    def /(den: Int) = Polynom(ms.map { _ / den }).squash
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
