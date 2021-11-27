package hackerrankfp.d200612_10

/** https://www.hackerrank.com/challenges/simplify-the-algebraic-expressions/problem */
object SimplifyAlgebraicExpressions {

  case class Monom(k: Int, p: Int) {
    def isNeg = k < 0
    def isZero = k == 0
    def sign: String = if (isNeg) "-" else "+"
    def mkStringWoSign: String = {
      val absk = math.abs(k)
      val xs = p match {
        case 0 => ""
        case 1 => "x"
        case p => s"x^$p"
      }
      val ks = if (absk != 1) s"$absk" else ""
      if (!isZero) s"$ks$xs" else ""
    }
    def mkString: String = isNeg match {
      case true  => s"$sign$mkStringWoSign"
      case false => mkStringWoSign
    }
    override def toString: String = (isNeg, !isZero) match {
      case (true, _) | (_, true) => s"$sign$mkStringWoSign"
      case _                     => ""
    }
  }
  object Monom {
    implicit class MonomOps(m1: Monom) {
      def unary_- = m1.copy(k = -m1.k)
      def +(m2: Monom): Polynom = Polynom(Seq(m1, m2)).squash
      def *(m2: Monom): Monom = Monom(m1.k * m2.k, m1.p + m2.p)
      def /(m2: Monom): Monom = Monom(m1.k / m2.k, m1.p - m2.p)
      def /(den: Int): Monom = /(Monom(den, 0))
      def toPolynom = new Polynom(m1.k, m1.p)
    }
    implicit val ordering: Ordering[Monom] = (x, y) => y.p - x.p
  }
  case class Polynom(ms: Seq[Monom]) {
    def this(k: Int, p: Int) = this(Seq(Monom(k, p)))
    def sorted = Polynom(ms.sorted)
    def isZero = ms == Nil
    def unary_- = Polynom(ms.map { _.unary_- })
    def squash = Polynom(
      ms.groupMapReduce(_.p)(_.k)(_ + _)
        .map { case (p, k) => Monom(k, p) }
        .filter { !_.isZero }
        .toVector
        .sorted,
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
    override def toString: String = (ms.head.mkString ++ ms.tail.map { _.toString }) mkString ""
    def toStringHR: String = (ms.head.mkString ++ ms.tail.map { m => s" ${m.sign} ${m.mkStringWoSign}" }) mkString ""
  }

  sealed trait Expr[+A]
  final case class Value[A](x: A) extends Expr[A]
  final case class BiOp[A](op: Char, l: Expr[A], r: Expr[A]) extends Expr[A]

  import Parsers._

  case class NP(n: Int, p: Int)

  object MonomParser {
    import ParserImpl.number
    import ParserImpl.char
    import ParserImpl.attempt
    import ParserImpl.succeed
    import ParserImpl.syntaxForParser

    val n: Parser[Int] = number
    val x: Parser[Char] = char('x')
    val p: Parser[Int] = char('^') *> number
    val nxp: Parser[NP] = (n <* x) ** p map { case (n, p) => NP(n, p) }
    val nx1: Parser[NP] = n <* x map { NP(_, 1) }
    val n_ : Parser[NP] = n map { NP(_, 0) }
    val xp: Parser[NP] = x *> p map { NP(1, _) }
    val x_ : Parser[NP] = x *> succeed(NP(1, 1))
    val monom: Parser[NP] = attempt(nxp) | attempt(nx1) | attempt(n_) | attempt(xp) | attempt(x_)
  }

  object MathOpToPolynomParser {
    import ParserImpl._

    def process(t: (Expr[Polynom], Seq[(Char, Expr[Polynom])])): Expr[Polynom] = t match {
      case (n, Nil) => n
      case (a, l)   => l.foldLeft(a) { case (acc, (op, x)) => BiOp(op, acc, x) }
    }
    val plusOrMinus: Parser[Char] = char('+') | char('-')
    val mulOrDiv: Parser[Char] = char('*') | char('/')
    def value: Parser[Expr[Polynom]] = MonomParser.monom.map { case NP(n, p) => Value(Monom(n, p).toPolynom) }
    def parens: Parser[Expr[Polynom]] = surround(char('('), char(')'))(addSub)
    def block: Parser[Expr[Polynom]] = value | parens
    def divMulExplicit: Parser[Expr[Polynom]] = (block ** (mulOrDiv ** block).many).map(process)
    def mulImplicit: Parser[Expr[Polynom]] = (value ** block).map { case (x1, x2) => process(x1, List(('*', x2))) }
    def divMul: Parser[Expr[Polynom]] = attempt(mulImplicit) | divMulExplicit
    def addSub: Parser[Expr[Polynom]] = (divMul ** (plusOrMinus ** divMul).many).map(process)
    def wholeCombination: Parser[Expr[Polynom]] = root(addSub)
  }

  def evalNode(node: Expr[Polynom]): Polynom = node match {
    case Value(x) => x
    case BiOp(op, l: Expr[Polynom], r: Expr[Polynom]) =>
      op match {
        case '+' => evalNode(l) + evalNode(r)
        case '-' => evalNode(l) - evalNode(r)
        case '*' => evalNode(l) * evalNode(r)
        case '/' => evalNode(l) / evalNode(r)
      }
  }

  import scala.util.chaining.scalaUtilChainingOps
  val exprParser: Parser[Expr[Polynom]] = MathOpToPolynomParser.wholeCombination
  val parser: String => Either[ParseError, Expr[Polynom]] = ParserImpl.run(exprParser)
  def simplify(ex: String) = {
    ex
      .replaceAll("\\s", "")
      .pipe { s =>
        if (s.startsWith("-")) "0" + s else s // TODO: hacky :)
      }
      .pipe(parser)
      .map(evalNode)
      .map(_.toStringHR)
      .fold(_ => ???, identity)
  }

  def body(line: => String): Unit = {
    val N = line.toInt
    (1 to N).map(_ => line).map(simplify).foreach(println)
  }

//  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }

  def main(p: Array[String]): Unit = processFile("parser2.txt", body)
  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
    scala
      .util
      .Using(
        scala.io.Source.fromFile(file),
      ) { src =>
        val it = src.getLines().map(_.trim)
        try { process(it.next()) }
        catch { case x: Throwable => x.printStackTrace() }
      }
      .fold(_ => ???, identity)
  }

}
