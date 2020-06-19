package hackerrankfp.d200612_10

object SimplifyAlgebraicExpressions {

  case class Monom(k: Int, p: Int) {
    def isNeg = k < 0
    def isZero = k == 0

    def sign: String = if (isNeg) "-" else "+"
    // string representations without any signs
    def mkStringWoSign: String = {
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
      if (!isZero) s"$ks$xs" else ""
    }

    // with `-` only
    def mkString: String = isNeg match {
      case true  => s"$sign$mkStringWoSign"
      case false =>         mkStringWoSign
    }

    // with `+` or `-`
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
    def sorted = Polynom(ms sorted)
    def isZero = ms == Nil
    def unary_- = Polynom(ms map { _.unary_- })
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
  
  implicit class StrNorm(s: String) {
    def ws: String = s.replaceAll("\\s", "")
  }

  trait ParsersBase[Parser[+_]] {
    import scala.util.matching.Regex // 6
    def run[A](p: Parser[A])(input: String): Either[ParseError, A]
    def succeed[A](a: A): Parser[A]
    def fail[A](msg: String): Parser[A]
    implicit def string(s: String): Parser[String]
    def or[A](p1: Parser[A], p2: => Parser[A]): Parser[A]
    def flatMap[A, B](pa: Parser[A])(g: A => Parser[B]): Parser[B]
    implicit def regex(r: Regex): Parser[String]
    def scope[A](msg: String)(p: Parser[A]): Parser[A]
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

  def firstNonmatchingIndex(s0: String, pat: String, s0_off: Int): Int = {
    var i = 0
    while (s0_off+i < s0.length && i < pat.length) {
      if (s0.charAt(s0_off+i) != pat.charAt(i)) return i
      i += 1
    }
    if (s0.length-s0_off >= pat.length) -1
    else s0.length-s0_off
  }

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
    // add new message at location
    def push(loc: Location, msg: String): ParseError = copy(stack = (loc, msg) :: stack)
    // label last error ????
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

    /* Builds a collapsed version of the given error stack -
     * messages at the same location have their messages merged,
     * separated by semicolons */
    def collapseStack(s: List[(Location,String)]): List[(Location,String)] =
      s.groupBy(_._1)
        .view.mapValues(_.map(_._2).mkString("; ")).
        toList.sortBy(_._1.offset)

    def formatLoc(l: Location): String = s"${l.line}.${l.col}"
  }

  trait Parsers[Parser[+_]] extends ParsersBase[Parser] { self =>
    import java.util.regex.Pattern // 23

    implicit def syntaxForParser[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)
    implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))
    def char(c: Char): Parser[Char] = string(c.toString) map { _.charAt(0) }
    def many[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p)) { _ :: _ } or succeed(Nil)
    def many1[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p)) { _ :: _ }
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
    def opt[A](p: Parser[A]): Parser[Option[A]] = p.map(Some(_)) or succeed(None)
    def whitespace: Parser[String] = "\\s*".r
    def digits: Parser[String] = "\\d+".r
    def thru(s: String): Parser[String] = (".*?"+Pattern.quote(s)).r
    def quoted: Parser[String] = string("\"") *> thru("\"").map(_.dropRight(1))
    def escapedQuoted: Parser[String] = token(quoted label "string literal")
    def digitsSigned: Parser[String] = token("[-+]?[0-9]+".r)
    def integer: Parser[Int] = digitsSigned map { _.toInt } label "integer literal"
    def integerWoSign: Parser[Int] = digits map { _.toInt } label "integer w/o sign literal"
    def long: Parser[Long] = digitsSigned map { _.toLong } label "long literal"
    def doubleString: Parser[String] = token("[-+]?([0-9]*\\.)?[0-9]+([eE][-+]?[0-9]+)?".r)
    def double: Parser[Double] = doubleString map { _.toDouble } label "double literal"
    def token[A](p: Parser[A]): Parser[A] = attempt(p) <* whitespace
    def sep[A](p: Parser[A], p2: Parser[Any]): Parser[List[A]] = sep1(p, p2) or succeed(List())
    def sep1[A](p: Parser[A], p2: Parser[Any]): Parser[List[A]] = map2(p, many(p2 *> p))(_ :: _)
    def opL[A](p: Parser[A])(op: Parser[(A,A) => A]): Parser[A] = map2(p, many(op ** p))((h,t) => t.foldLeft(h)((a,b) => b._1(a,b._2)))
    def surround[A](start: Parser[Any], stop: Parser[Any])(p: => Parser[A]) = start *> p <* stop
    def eof: Parser[String] = regex("\\z".r).label("unexpected trailing characters")
    def root[A](p: Parser[A]): Parser[A] = p <* eof

    case class ParserOps[A](p: Parser[A]) {
      def | [B>:A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
      def or[B>:A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
      def map[B](f: A => B): Parser[B] = self.map(p)(f)
      def many:  Parser[List[A]] = self.many(p)
      def many1: Parser[List[A]] = self.many1(p)
      def slice: Parser[String] = self.slice(p)
      def **     [B](pb: => Parser[B]): Parser[(A, B)] = self.product(p, pb)
      def product[B](pb: => Parser[B]): Parser[(A, B)] = self.product(p, pb)
      def flatMap[B](f: A => Parser[B]): Parser[B] = self.flatMap(p)(f)
      def label(msg: String): Parser[A] = self.label(msg)(p)
      def scope(msg: String): Parser[A] = self.scope(msg)(p)
      def *>[B](p2: => Parser[B])  : Parser[B] = self.skipL(p, p2)
      def <*   (p2: => Parser[Any]): Parser[A] = self.skipR(p, p2)
      def token: Parser[A] = self.token(p)
      def sep (separator: Parser[Any]): Parser[List[A]] = self.sep (p, separator)
      def sep1(separator: Parser[Any]): Parser[List[A]] = self.sep1(p, separator)
      def as[B](b: B): Parser[B] = self.map(self.slice(p))(_ => b)
      def opL(op: Parser[(A,A) => A]): Parser[A] = self.opL(p)(op)
    }
  }
  
  object Reference extends Parsers[Parser] {
    import scala.util.matching.Regex
    
    // 0.
    def run[A](p: Parser[A])(s: String): Either[ParseError,A] = {
      val s0 = ParseState(Location(s))
      p(s0).extract
    }

    // 0. debug version
    def runLen[A](p: Parser[A])(s: String): Either[ParseError, (A, Int)] = {
      val s0 = ParseState(Location(s))
      p(s0).extractLen
    }

    // 1.
    def succeed[A](a: A): Parser[A] = _ =>
      Success(a, 0)

    // 2.
    def fail[A](msg: String): Parser[A] = s =>
      Failure(s.loc.toError(msg), true)

    // 3.
    implicit def string(w: String): Parser[String] = s =>
      firstNonmatchingIndex(s.loc.input, w, s.loc.offset) match {
        case -1 => Success(w, w.length) // they matched
        case i => Failure(s.loc.advanceBy(i).toError(s"'$w'"), i != 0)
      }

    // 4.
    def or[A](p: Parser[A], p2: => Parser[A]): Parser[A] = s =>
      p(s) match {
        case Failure(_, false) => p2(s)
        // if Failure(_, true) => we don't need to try another parser
        // if Success          => we don't need to try another parser
        case r => r
      }

    // 5.
    def flatMap[A, B](pa: Parser[A])(g: A => Parser[B]): Parser[B] = s =>
      pa(s) match {
        case Success(a, pos) =>
          val pb: Parser[B] = g(a)
          // try to apply 2nd by providing shifted data
          val rb1 = pb(s.advanceBy(pos))
          // fix commit if hasn't fixed
          val rb2 = rb1.addCommit(pos != 0)
          val rb3 = rb2.advanceSuccess(pos)
          rb3
        // if 1st fail => we don't need to try 2nd
        case f @ Failure(_,_) => f
      }

    // 6.
    // failures are uncommitted
    def regex(r: Regex): Parser[String] = s =>
      r.findPrefixOf(s.input) match {
        case Some(m) => Success(m, m.length)
        case None    => Failure(s.loc.toError(s"regex $r"), isCommitted = false)
      }

    // 7
    def scope[A](msg: String)(p: Parser[A]): Parser[A] = s =>
      p(s).mapError(_.push(s.loc, msg))

    // 8
    def label[A](msg: String)(p: Parser[A]): Parser[A] = s =>
      p(s).mapError(_.label(msg))

    // 9
    def attempt[A](p: Parser[A]): Parser[A] = s =>
      p(s).uncommit

    // 10
    def slice[A](p: Parser[A]): Parser[String] = s =>
      p(s) match {
        case Success(_,n) => Success(s.slice(n),n)
        case f @ Failure(_,_) => f
      }

    /**
      * overridden version of `many` that accumulates
      * the list of results using a monolithic loop.
      * This avoids stack overflow errors for most grammars.
      *
      */
    override def many[A](p: Parser[A]): Parser[List[A]] =
      s => {
        val buf = new collection.mutable.ListBuffer[A]

        def go(p: Parser[A], offset: Int): Result[List[A]] =
          p(s.advanceBy(offset)) match {
            case Success(a,n)         => buf += a; go(p, offset+n)
            case f @ Failure(_, true) => f
            case     Failure(_, _)    => Success(buf.toList,offset)
          }

        go(p, 0)
      }
  }

  sealed trait Expr[+A]
  final case class Value[A](x: A) extends Expr[A]
  final case class BiOp[A, EA >: Expr[A]](op: Char, l: EA, r: EA) extends Expr[A]

  def mkNode[A, EA >: Expr[A]](op: Char, n1: EA, n2: EA): EA = BiOp(op, n1, n2)

  case class NP(n: Int, p: Int)

  object MonomParser {
    import Reference._

    val n: Parser[Int] = integerWoSign
    val x: Parser[Char] = char('x')
    val p: Parser[Int] = char('^') *> integerWoSign
    val nxp: Parser[NP] = (n <* x) ** p map { case (n, p) => NP(n, p) }
    val nx1: Parser[NP] = n <* x map { NP(_, 1) }
    val n_ : Parser[NP] = n map { NP(_, 0) }
    val xp : Parser[NP] = x *> p map { NP(1, _) }
    val x_ : Parser[NP] = x *> succeed(NP(1, 1))
    val monom: Parser[NP] = attempt(nxp) | attempt(nx1) | attempt(n_) | attempt(xp) | attempt(x_)
  }

  trait AbstractMathParser[A] {
    import Reference._
    def process[EA >: Expr[A]](t: (EA, Seq[(Char, EA)])): EA = t match {
      case (n, Nil) => n
      case (a, l) => l.foldLeft(a) { case (acc, (op, x)) => mkNode(op, acc, x) }
    }
    val plusOrMinus: Parser[Char] = char('+') | char('-')
    val mulOrDiv: Parser[Char] = char('*') | char('/')
    def value: Parser[Expr[A]]
    def parens = surround(char('('), char(')'))(addSub)
    def factor = value | parens
    def divMul = ( factor ** (mulOrDiv ** factor).many ).map(process)
    def addSub: Parser[Expr[A]] = ( divMul ** (plusOrMinus ** divMul).many ).map(process)
    def built = root(addSub)
  }

  object MathOpToPolynomParser extends AbstractMathParser[Polynom] {
    import Reference.syntaxForParser
    
    override def value: Parser[Expr[Polynom]] = MonomParser.monom.map { case NP(n,p) => Value(Monom(n, p).toPolynom) }
  }

  def eval(root: Expr[Polynom]): Polynom = {
    def evalOp(op: Char, l: Expr[Polynom], r: Expr[Polynom]): Polynom = op match {
      case '+' => evalNode(l) + evalNode(r)
      case '-' => evalNode(l) - evalNode(r)
      case '*' => evalNode(l) * evalNode(r)
      case '/' => evalNode(l) / evalNode(r)
    }
    def evalNode(node: Expr[Polynom]): Polynom = node match {
      case Value(x) => x
      case BiOp(op, l:Expr[Polynom], r:Expr[Polynom]) => evalOp(op, l,  r)
    }
    evalNode(root)
  }
  
  def simplify(ex: String) =
    Reference.run(MathOpToPolynomParser.built)(ex.ws).map { eval } map { _.toStringHR } fold (_ => ???, identity)

  def process(data: List[String]) = data map simplify

  def body(line: => String): Unit = {
    val N = line.toInt
    val list = (1 to N).map { _ => line }.toList
    val r = process(list)
    r.foreach { println }
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
