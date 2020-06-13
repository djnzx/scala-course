package hackerrankfp.d200612_10

import scala.collection.mutable

object SimplifyAlgebraicExpressions {
  
  sealed trait Expression
  
  // representation
  case class Monom(k: Int, p: Int) extends Expression {
    def isNeg: Boolean = k < 0
    def isZero: Boolean = k == 0
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
      // k
      val ks = if (absk != 1) s"$absk" else ""
      if (k != 0) s"$ss$ks$xs" else ""
    }
  }
  
  case class Polynom(ms: Seq[Monom]) extends Expression {
    def negate = Polynom(ms map { _.unary_- })
    def sort = Polynom(ms sortBy { -_.p } )
    def squash = Polynom(ms.groupBy(_.p)
      .map { case (p, sm) => (sm.map { _.k } .sum, p) }
      .filter { case (k, _) => k != 0 }
      .toVector
      .sortBy { case (_, p) => -p }
      .map { case (k, p) => Monom(k, p) }
    )
    def +(p2: Polynom) = Polynom(ms ++ p2.ms).squash
    def /(den: Int) = Polynom(ms.map { _ / den })
    def *(p2: Polynom) = Polynom(for {
      m1 <- ms
      m2 <- p2.ms
    } yield m1 * m2).squash
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
    override def toString: String =
      (ms.head.mkString ++ ms.tail.map { _.toString }) mkString ""
  }
  
  case class IntVal(x: Int) extends Expression

  sealed trait Op extends Expression
  case class Add(a: Expression, b: Expression) extends Op
  case class Sub(a: Expression, b: Expression) extends Op
  case class Mul(a: Expression, b: Expression) extends Op
  case class Div(a: Expression, b: Expression) extends Op

  object Polynom {
    def of(mst: (Int, Int)*) = this(mst.map { case (k, p) => Monom(k, p) })
  }
  
  object MonomTest {
    import Console._
    val m1 = Monom(2,3)
    val m2 = Monom(5,2)
    val m3 = m1 * m2
    val test2 = () => {
      println(m1.toString)
      println(m2.toString)
      println(m3.toString)
    }
  }
  object PolynomTest {
    val p = Polynom.of((-12,3), (4,1), (-3,2), (-5,0))
    val test1 = () => {
      println(p.toString)
      println(p.sort.toString)
      println(p.sort.negate.toString)
    }
    val p2 = Polynom.of((2,3), (3,3), (1,4), (-1,4))
    val test2 = () => {
      println(p2.toString)
      println(p2.squash.toString)
    }
    val p31 = Polynom.of((2,1), (3,2))
    val p32 = Polynom.of((3,3), (5,4))
    val p33 = p31 * p32
    val test3 = () => {
      println(p31.toString)
      println(p32.toString)
      println(p33.toString)
    }
    val test4 = () => {
      val p41 = Polynom.of((2,2), (3,3))
      val p42 = Polynom.of((3,2), (4,3))
      val p43 = p41 + p42
      val p5 = Polynom.of((1,0), (1,0), (1,0))
      println(p41.toString)
      println(p42.toString)
      println(p43.toString)
      println(p5.squash.toString)
    }
  }
  
  /** core implementation */
  def process(data: List[String]) = {
    data
  }

  def body(line: => String): Unit = {
    val N = line.toInt
    val list = (1 to N).map { _ => line }.toList
    val r = process(list)
    r.foreach { println }
  }

  /** main to run from the console */
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }
  /** main to run from file */
  def main(p: Array[String]): Unit = processFile("parser1.txt", body)
  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
    scala.util.Using(
      scala.io.Source.fromFile(file)
    ) { src =>
      val it = src.getLines().map(_.trim)
      try { process(it.next()) }
      catch { case x: Throwable => x.printStackTrace() }
    }.fold(_ => ???, identity)
  }

}
