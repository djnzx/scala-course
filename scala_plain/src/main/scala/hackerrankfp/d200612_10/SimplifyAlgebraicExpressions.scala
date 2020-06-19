package hackerrankfp.d200612_10

/**
  * https://www.hackerrank.com/challenges/simplify-the-algebraic-expressions/problem
  */
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
      case false =>         mkStringWoSign
    }
    override def toString: String = (isNeg, !isZero) match {
      case (true, _) |
           (_, true) => s"$sign$mkStringWoSign"
      case _         => ""
    }
  }
  object Monom {
    implicit class MonomOps(m1: Monom) {
      def unary_-  = m1.copy(k = - m1.k)
      def +(m2: Monom): Polynom = Polynom(Seq(m1, m2)).squash
      def *(m2: Monom): Monom = Monom(m1.k * m2.k, m1.p + m2.p)
      def /(m2: Monom): Monom = Monom(m1.k / m2.k, m1.p - m2.p)
      def /(den: Int): Monom = / (Monom(den, 0))
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
    override def toString: String = (ms.head.mkString ++ ms.tail.map { _.toString }) mkString ""
    def toStringHR: String = (ms.head.mkString ++ ms.tail.map { m => s" ${m.sign} ${m.mkStringWoSign}" }) mkString ""
  }

  sealed trait Expr[+A]
  final case class Value[A](x: A) extends Expr[A]
  final case class BiOp[A](op: Char, l: Expr[A], r: Expr[A]) extends Expr[A]

  case class NP(n: Int, p: Int)

  trait ParsersBase[Parser[+_]] {
    import scala.util.matching.Regex
    def run[A](p: Parser[A])(input: String): Either[ParseError, A]
    def succeed[A](a: A): Parser[A]
    def fail[A](msg: String): Parser[A]
    implicit def string(s: String): Parser[String]
    def or[A](p1: Parser[A], p2: => Parser[A]): Parser[A]
    def flatMap[A, B](pa: Parser[A])(g: A => Parser[B]): Parser[B]
    implicit def regex(r: Regex): Parser[String]
    def label[A](msg: String)(p: Parser[A]): Parser[A]
    def attempt[A](p: Parser[A]): Parser[A]
    def slice[A](p: Parser[A]): Parser[String]
  }

  type Parser[+A] = ParseState => Result[A]

  case class ParseState(loc: Location) {
    def advanceBy(numChars: Int) = copy(loc = loc.copy(offset = loc.offset + numChars))
    def input = loc.input.substring(loc.offset)
    def slice(n: Int) = loc.input.substring(loc.offset, loc.offset + n)
  }

  sealed trait Result[+A] {
    def extract: Either[ParseError,A] = this match {
      case Failure(e,_) => Left(e)
      case Success(a,_) => Right(a)
    }
    def extractLen: Either[ParseError,(A, Int)] = this match {
      case Failure(e,_) => Left(e)
      case Success(a, l) => Right((a, l))
    }
    def uncommit: Result[A] = this match {
      case Failure(e, true) => Failure(e, isCommitted = false)
      case _ => this
    }
    def addCommit(isCommitted: Boolean): Result[A] = this match {
      case Failure(e,c) => Failure(e, c || isCommitted)
      case _ => this
    }
    def mapError(f: ParseError => ParseError): Result[A] = this match {
      case Failure(e,c) => Failure(f(e),c)
      case _ => this
    }
    def advanceSuccess(n: Int): Result[A] = this match {
      case Success(a,m) => Success(a,n+m)
      case _ => this
    }
  }
  case class Success[+A](get: A, pos: Int) extends Result[A]
  case class Failure(get: ParseError, isCommitted: Boolean) extends Result[Nothing]

  case class Location(input: String, offset: Int = 0) {
    lazy val line: Int = input.slice(0,offset+1).count(_ == '\n') + 1
    lazy val col: Int = input.slice(0,offset+1).lastIndexOf('\n') match {
      case -1 => offset + 1
      case lineStart => offset - lineStart
    }

    def toError(msg: String): ParseError = ParseError(List((this, msg)))
    def advanceBy(n: Int): Location = copy(offset = offset + n)
    def currentLine: String =
      if (input.length > 1) input.linesIterator.drop(line-1).next
      else ""

    def columnCaret: String = (" " * (col-1)) + "^"
  }

  case class ParseError(stack: List[(Location,String)]) {
    def push(loc: Location, msg: String): ParseError = copy(stack = (loc, msg) :: stack)
    def label[A](s: String): ParseError = ParseError(latestLoc.map { loc => (loc, s) } .toList)
    def latestLoc: Option[Location] = latest map { case (l, _) => l }
    def latest: Option[(Location,String)] = stack.lastOption

    override def toString =
      if (stack.isEmpty) "no error messages"
      else {
        val collapsed = collapseStack(stack)
        val context =
          collapsed.lastOption.map("\n\n" + _._1.currentLine).getOrElse("") +
            collapsed.lastOption.map("\n" + _._1.columnCaret).getOrElse("")

        collapsed.map { case (loc,msg) => loc.line.toString + "." + loc.col + " " + msg }.mkString("\n") +
          context
      }

    def collapseStack(s: List[(Location,String)]): List[(Location,String)] =
      s.groupBy(_._1)
        .view.mapValues(_.map(_._2).mkString("; ")).
        toList.sortBy(_._1.offset)

    def formatLoc(l: Location): String = s"${l.line}.${l.col}"
  }

  trait Parsers[Parser[+_]] extends ParsersBase[Parser] { self =>
    implicit def syntaxForParser[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)
    implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))
    def char(c: Char): Parser[Char] = string(c.toString) map { _.charAt(0) }
    def many[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p)) { _ :: _ } | succeed(Nil)
    def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] =
      if (n<=0) succeed(Nil)
      else map2(p, listOfN(n-1, p)) { _ :: _ }
    def product[A, B](pa: Parser[A], pb: => Parser[B]): Parser[(A, B)] = for {
      a <- pa
      b <- pb
    } yield (a, b)
    def map2[A, B, C](pa: Parser[A], pb: => Parser[B])(f: (A, B) => C): Parser[C] = for {
      a <- pa
      b <- pb
      c = f(a,b)
    } yield c
    def map[A,B](pa: Parser[A])(f: A => B): Parser[B] = flatMap(pa) { f andThen succeed }
    def skipL[B](p: Parser[Any], p2: => Parser[B]): Parser[B] = map2(slice(p), p2)((_,b) => b)
    def skipR[A](p: Parser[A], p2: => Parser[Any]): Parser[A] = map2(p, slice(p2))((a,_) => a)
    def whitespace: Parser[String] = "\\s*".r
    def digits: Parser[String] = "\\d+".r
    def number: Parser[Int] = digits map { _.toInt } label "integer w/o sign literal"
    def doubleString: Parser[String] = token("[-+]?([0-9]*\\.)?[0-9]+([eE][-+]?[0-9]+)?".r)
    def double: Parser[Double] = doubleString map { _.toDouble } label "double literal"
    def token[A](p: Parser[A]): Parser[A] = attempt(p) <* whitespace
    def surround[A](start: Parser[Any], stop: Parser[Any])(p: => Parser[A]) = start *> p <* stop
    def eof: Parser[String] = regex("\\z".r).label("unexpected trailing characters")
    def root[A](p: Parser[A]): Parser[A] = p <* eof

    case class ParserOps[A](p: Parser[A]) {
      def | [B>:A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
      def map[B](f: A => B): Parser[B] = self.map(p)(f)
      def many:  Parser[List[A]] = self.many(p)
      def slice: Parser[String] = self.slice(p)
      def **     [B](pb: => Parser[B]): Parser[(A, B)] = self.product(p, pb)
      def product[B](pb: => Parser[B]): Parser[(A, B)] = self.product(p, pb)
      def flatMap[B](f: A => Parser[B]): Parser[B] = self.flatMap(p)(f)
      def label(msg: String): Parser[A] = self.label(msg)(p)
      def *>[B](p2: => Parser[B])  : Parser[B] = self.skipL(p, p2)
      def <*   (p2: => Parser[Any]): Parser[A] = self.skipR(p, p2)
    }
  }
  
  object Reference extends Parsers[Parser] {
    import scala.util.matching.Regex

    def run[A](p: Parser[A])(s: String): Either[ParseError, A] = p(ParseState(Location(s))).extract

    def runLen[A](p: Parser[A])(s: String): Either[ParseError, (A, Int)] =
      p(ParseState(Location(s))).extractLen

    def succeed[A](a: A): Parser[A] = _ => Success(a, 0)

    def fail[A](msg: String): Parser[A] = s => Failure(s.loc.toError(msg), true)

    def firstNonmatchingIndex(s0: String, pat: String, s0_off: Int): Int = {
      var i = 0
      while (s0_off+i < s0.length && i < pat.length) {
        if (s0.charAt(s0_off+i) != pat.charAt(i)) return i
        i += 1
      }
      if (s0.length-s0_off >= pat.length) -1
      else s0.length-s0_off
    }

    implicit def string(w: String): Parser[String] = s =>
      firstNonmatchingIndex(s.loc.input, w, s.loc.offset) match {
        case -1 => Success(w, w.length) // they matched
        case i => Failure(s.loc.advanceBy(i).toError(s"'$w'"), i != 0)
      }

    def or[A](p: Parser[A], p2: => Parser[A]): Parser[A] = s =>
      p(s) match {
        case Failure(_, false) => p2(s)
        case r => r
      }

    def flatMap[A, B](pa: Parser[A])(g: A => Parser[B]): Parser[B] = s =>
      pa(s) match {
        case Success(a, pos) =>
          val pb: Parser[B] = g(a)
          val rb1 = pb(s.advanceBy(pos))
          val rb2 = rb1.addCommit(pos != 0)
          val rb3 = rb2.advanceSuccess(pos)
          rb3
        case f@Failure(_, _) => f
      }

    def regex(r: Regex): Parser[String] = s =>
      r.findPrefixOf(s.input) match {
        case Some(m) => Success(m, m.length)
        case None => Failure(s.loc.toError(s"regex $r"), isCommitted = false)
      }

    def scope[A](msg: String)(p: Parser[A]): Parser[A] = s =>
      p(s).mapError(_.push(s.loc, msg))

    def label[A](msg: String)(p: Parser[A]): Parser[A] = s =>
      p(s).mapError(_.label(msg))

    def attempt[A](p: Parser[A]): Parser[A] = s => p(s).uncommit

    def slice[A](p: Parser[A]): Parser[String] = s =>
      p(s) match {
        case Success(_, n) => Success(s.slice(n), n)
        case f@Failure(_, _) => f
      }
  }
  
  object MonomParser {
    import Reference.{number, char, attempt, succeed, syntaxForParser}

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
    import Reference._
    
    def process(t: (Expr[Polynom], Seq[(Char, Expr[Polynom])])): Expr[Polynom] = t match {
      case (n, Nil) => n
      case (a, l) => l.foldLeft(a) { case (acc, (op, x)) => BiOp(op, acc, x) }
    }
    val plusOrMinus: Parser[Char] = char('+') | char('-')
    val mulOrDiv: Parser[Char] = char('*') | char('/')
    def value: Parser[Expr[Polynom]] = MonomParser.monom.map { case NP(n,p) => Value(Monom(n, p).toPolynom) }
    def parens: Parser[Expr[Polynom]] = surround(char('('), char(')'))(addSub)
    def factor: Parser[Expr[Polynom]] = value | parens
    def divMul: Parser[Expr[Polynom]] = ( factor ** (mulOrDiv ** factor).many ).map(process)
    def addSub: Parser[Expr[Polynom]] = ( divMul ** (plusOrMinus ** divMul).many ).map(process)
    def built: Parser[Expr[Polynom]] = root(addSub)
  }

  def evalNode(node: Expr[Polynom]): Polynom = node match {
    case Value(x) => x
    case BiOp(op, l:Expr[Polynom], r:Expr[Polynom]) => op match {
      case '+' => evalNode(l) + evalNode(r)
      case '-' => evalNode(l) - evalNode(r)
      case '*' => evalNode(l) * evalNode(r)
      case '/' => evalNode(l) / evalNode(r)
    }
  }
  
  def simplify(ex: String) = Reference.run(MathOpToPolynomParser.built)(
    ex.replaceAll("\\s", ""))
    .map(evalNode)
    .map(_.toStringHR)
    .fold(_ => ???, identity)

  def body(line: => String): Unit = {
    val N = line.toInt
    (1 to N).map(_ => line).map(simplify).foreach(println)
  }

  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }

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
