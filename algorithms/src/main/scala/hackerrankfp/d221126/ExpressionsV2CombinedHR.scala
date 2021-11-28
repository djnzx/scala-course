package hackerrankfp.d221126

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.annotation.tailrec
import scala.util.Try
import scala.util.Using

/** https://www.hackerrank.com/challenges/expressions-v2/problem */
object ExpressionsV2CombinedHR {

  object Parsers {

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
      def extract: Either[ParseError, A] = this match {
        case Failure(e, _) => Left(e)
        case Success(a, _) => Right(a)
      }
      def extractLen: Either[ParseError, (A, Int)] = this match {
        case Failure(e, _) => Left(e)
        case Success(a, l) => Right((a, l))
      }
      def uncommit: Result[A] = this match {
        case Failure(e, true) => Failure(e, isCommitted = false)
        case _                => this
      }
      def addCommit(isCommitted: Boolean): Result[A] = this match {
        case Failure(e, c) => Failure(e, c || isCommitted)
        case _             => this
      }
      def mapError(f: ParseError => ParseError): Result[A] = this match {
        case Failure(e, c) => Failure(f(e), c)
        case _             => this
      }
      def advanceSuccess(n: Int): Result[A] = this match {
        case Success(a, m) => Success(a, n + m)
        case _             => this
      }
    }
    case class Success[+A](get: A, pos: Int) extends Result[A]
    case class Failure(get: ParseError, isCommitted: Boolean) extends Result[Nothing]

    case class Location(input: String, offset: Int = 0) {
      lazy val line: Int = input.slice(0, offset + 1).count(_ == '\n') + 1
      lazy val col: Int = input.slice(0, offset + 1).lastIndexOf('\n') match {
        case -1        => offset + 1
        case lineStart => offset - lineStart
      }

      def toError(msg: String): ParseError = ParseError(List((this, msg)))
      def advanceBy(n: Int): Location = copy(offset = offset + n)
      def currentLine: String =
        if (input.length > 1) input.linesIterator.drop(line - 1).next
        else ""

      def columnCaret: String = (" " * (col - 1)) + "^"
    }

    case class ParseError(stack: List[(Location, String)]) {
      def push(loc: Location, msg: String): ParseError = copy(stack = (loc, msg) :: stack)
      def label[A](s: String): ParseError = ParseError(latestLoc.map { loc => (loc, s) }.toList)
      def latestLoc: Option[Location] = latest map { case (l, _) => l }
      def latest: Option[(Location, String)] = stack.lastOption

      override def toString =
        if (stack.isEmpty) "no error messages"
        else {
          val collapsed = collapseStack(stack)
          val context =
            collapsed.lastOption.map("\n\n" + _._1.currentLine).getOrElse("") +
              collapsed.lastOption.map("\n" + _._1.columnCaret).getOrElse("")

          collapsed.map { case (loc, msg) => loc.line.toString + "." + loc.col + " " + msg }.mkString("\n") +
            context
        }

      def collapseStack(s: List[(Location, String)]): List[(Location, String)] =
        s.groupBy(_._1)
          .view
          .mapValues(_.map(_._2).mkString("; "))
          .toList
          .sortBy(_._1.offset)

      def formatLoc(l: Location): String = s"${l.line}.${l.col}"
    }

    trait Parsers[P[+_]] extends ParsersBase[P] { self =>

      /** attaching syntax */
      implicit def syntaxForParser[A](p: P[A]): ParserOps[A] = ParserOps[A](p)
      implicit def asStringParser[A](a: A)(implicit f: A => P[String]): ParserOps[String] = ParserOps(f(a))
      def char(c: Char): P[Char] = string(c.toString) map { _.charAt(0) }
      def many[A](p: P[A]): P[List[A]] = map2(p, many(p)) { _ :: _ } | succeed(Nil)
      def listOfN[A](n: Int, p: P[A]): P[List[A]] =
        if (n <= 0) succeed(Nil)
        else map2(p, listOfN(n - 1, p)) { _ :: _ }
      def product[A, B](pa: P[A], pb: => P[B]): P[(A, B)] = for {
        a <- pa
        b <- pb
      } yield (a, b)
      def map2[A, B, C](pa: P[A], pb: => P[B])(f: (A, B) => C): P[C] = for {
        a <- pa
        b <- pb
      } yield f(a, b)
      def map[A, B](pa: P[A])(f: A => B): P[B] = flatMap(pa) { f andThen succeed }
      def skipL[B](p: P[Any], p2: => P[B]): P[B] = map2(slice(p), p2)((_, b) => b)
      def skipR[A](p: P[A], p2: => P[Any]): P[A] = map2(p, slice(p2))((a, _) => a)
      def opt[A](p: P[A]): P[Option[A]] = p.map(Some(_)) | succeed(None)
      def whitespace: P[String] = "\\s*".r
      def digits: P[String] = "\\d+".r
      def number: P[Int] = digits map { _.toInt } label "integer w/o sign literal"
      def doubleString: P[String] = token("[-+]?([0-9]*\\.)?[0-9]+([eE][-+]?[0-9]+)?".r)
      def double: P[Double] = doubleString map { _.toDouble } label "double literal"
      def token[A](p: P[A]): P[A] = attempt(p) <* whitespace
      def surround[A](start: P[Any], stop: P[Any])(p: => P[A]) = start *> p <* stop
      def eof: P[String] = regex("\\z".r).label("unexpected trailing characters")
      def root[A](p: P[A]): P[A] = p <* eof

      /** syntax */
      case class ParserOps[A](p: P[A]) {
        def |[B >: A](p2: => P[B]): P[B] = self.or(p, p2)
        def map[B](f: A => B): P[B] = self.map(p)(f)
        def many: P[List[A]] = self.many(p)
        def slice: P[String] = self.slice(p)
        def product[B](pb: => P[B]): P[(A, B)] = self.product(p, pb)
        def **[B](pb: => P[B]): P[(A, B)] = self.product(p, pb)
        def flatMap[B](f: A => P[B]): P[B] = self.flatMap(p)(f)
        def label(msg: String): P[A] = self.label(msg)(p)
        def *>[B](p2: => P[B]): P[B] = self.skipL(p, p2)
        def <*(p2: => P[Any]): P[A] = self.skipR(p, p2)
        def opt: P[Option[A]] = self.opt(p)
      }
    }

    object ParserImpl extends Parsers[Parser] {
      import scala.util.matching.Regex

      def run[A](p: Parser[A])(s: String): Either[ParseError, A] = p(ParseState(Location(s))).extract

      def runLen[A](p: Parser[A])(s: String): Either[ParseError, (A, Int)] =
        p(ParseState(Location(s))).extractLen

      def succeed[A](a: A): Parser[A] = _ => Success(a, 0)

      def fail[A](msg: String): Parser[A] = s => Failure(s.loc.toError(msg), true)

      def firstNonMatchingIndex(s0: String, pat: String, s0_off: Int): Int = {
        var i = 0
        while (s0_off + i < s0.length && i < pat.length) {
          if (s0.charAt(s0_off + i) != pat.charAt(i)) return i
          i += 1
        }
        if (s0.length - s0_off >= pat.length) -1
        else s0.length - s0_off
      }

      implicit def string(w: String): Parser[String] = s =>
        firstNonMatchingIndex(s.loc.input, w, s.loc.offset) match {
          case -1 => Success(w, w.length) // they matched
          case i  => Failure(s.loc.advanceBy(i).toError(s"'$w'"), i != 0)
        }

      def or[A](p: Parser[A], p2: => Parser[A]): Parser[A] = s =>
        p(s) match {
          case Failure(_, false) => p2(s)
          case r                 => r
        }

      def flatMap[A, B](pa: Parser[A])(g: A => Parser[B]): Parser[B] = s =>
        pa(s) match {
          case Success(a, pos) =>
            val pb: Parser[B] = g(a)
            val rb1 = pb(s.advanceBy(pos))
            val rb2 = rb1.addCommit(pos != 0)
            val rb3 = rb2.advanceSuccess(pos)
            rb3
          case f @ Failure(_, _) => f
        }

      def regex(r: Regex): Parser[String] = s =>
        r.findPrefixOf(s.input) match {
          case Some(m) => Success(m, m.length)
          case None    => Failure(s.loc.toError(s"regex $r"), isCommitted = false)
        }

      def scope[A](msg: String)(p: Parser[A]): Parser[A] = s => p(s).mapError(_.push(s.loc, msg))

      def label[A](msg: String)(p: Parser[A]): Parser[A] = s => p(s).mapError(_.label(msg))

      def attempt[A](p: Parser[A]): Parser[A] = s => p(s).uncommit

      def slice[A](p: Parser[A]): Parser[String] = s =>
        p(s) match {
          case Success(_, n)     => Success(s.slice(n), n)
          case f @ Failure(_, _) => f
        }
    }

  }

  object Domain {

    sealed trait Op
    sealed trait Op1 extends Op
    case object Plus extends Op1
    case object Minus extends Op1
    object Op1 {
      def unapply(s: String): Option[Op1] = s match {
        case "+" => Some(Plus)
        case "-" => Some(Minus)
        case _   => None
      }
      def unapply(c: Char): Option[Op1] = unapply(c.toString)
    }

    sealed trait Op2 extends Op
    case object Add extends Op2
    case object Sub extends Op2
    case object Mul extends Op2
    case object Div extends Op2
    object Op2 {
      def unapply(s: String): Option[Op2] = s match {
        case "+" => Some(Add)
        case "-" => Some(Sub)
        case "*" => Some(Mul)
        case "/" => Some(Div)
        case _   => None
      }
      def unapply(c: Char): Option[Op2] = unapply(c.toString)
    }

    sealed trait Expr
    final case class Value(x: Int) extends Expr
    final case class UnOp(op: Op1, e: Expr) extends Expr
    final case class Neg(ex: Expr) extends Expr
    final case class BinOp(op: Op2, l: Expr, r: Expr) extends Expr

    val P: Int = 1000000007 // 10^9 + 7
    val P2: Int = P - 2 // 1000000005

    implicit class EuclideanModuloSyntax(private val a: Int) {
      def /%(b: Int): (Int, Int) = {
        val q = a / b
        val r = a - q * b

        /** euclidean modulo must be positive */
        if (r >= 0) (q, r)
        else { // r < 0
          if (a < 0 && b > 0) {
            val r1 = r + b
            val q1 = (a - r1) / b
            (q1, r1)
          } else {
            val q1 = q + math.signum(q)
            val r1 = a - q1 * b
            (q1, r1)
          }
        }
      }
      def %%(b: Int) = /%(b)._2
    }

    implicit class RichInt(private val i: Int) extends AnyVal {
      def emod: Int = i %% P
    }

    /** calculates 1/x === x^p-2^ */
    def inv(x: Int): Int = (1 to P2)
      .foldLeft(1) { (a, _) =>
        modMul(a, x)
      }

    def modAdd(a: Int, b: Int): Int = (a.emod + b.emod).emod
    def modSub(a: Int, b: Int): Int = (a.emod - b.emod).emod
    def modMul(a: Int, b: Int): Int = (a.emod * b.emod).emod
    def modDiv(a: Int, b: Int): Int = modMul(a, inv(b)).emod

    def evalNode(node: Expr): Int = node match {
      case Value(x) => x
      case Neg(ex)  => -evalNode(ex)
      case UnOp(op, ex: Expr) =>
        op match {
          case Minus => -evalNode(ex)
          case Plus  => +evalNode(ex)
        }
      case BinOp(op, l: Expr, r: Expr) =>
        op match {
          case Add => modAdd(evalNode(l), evalNode(r))
          case Sub => modSub(evalNode(l), evalNode(r))
          case Mul => modMul(evalNode(l), evalNode(r))
          case Div => modDiv(evalNode(l), evalNode(r))
        }
    }

  }

  object ExpressionParser {
    import Parsers._
    import Domain._
    import ParserImpl._

    /** fold sequence, right associative */
    def foldListRightAssoc(ex: Expr, ops: List[(Op2, Expr)]): Expr = ops match {
      case Nil            => ex
      case (op, ex2) :: t => BinOp(op, ex, foldListRightAssoc(ex2, t))
    }

    val foldRA: ((Expr, List[(Op2, Expr)])) => Expr = (foldListRightAssoc _).tupled

    def fold(xs: (Op2, List[(Char, Op2)])): Expr = ???

    /** plain chars */
    val plusOrMinus: Parser[Char] = char('+') | char('-')
    val mulOrDiv: Parser[Char] = char('*') | char('/')
    val plainValue: Parser[Value] = number.map(x => Value(x))
    val plainValueWithPlus: Parser[UnOp] = (char('+') *> plainValue) map { UnOp(Plus, _) }
    val plainValueWithMinus: Parser[UnOp] = (char('-') *> plainValue) map { UnOp(Minus, _) }

    /** plain implementation #1 */
    val plainValueWithUnary1: Parser[Expr] = plainValue | plainValueWithPlus | plainValueWithMinus
    // FIXME: implement via Parser.flatMap
    val unPlusOrMinus: Parser[Op1] = plusOrMinus.map(Op1.unapply).map(_.get)

    /** implementation #2 */
    val plainValueWithUnary2: Parser[Expr] = plainValue | (unPlusOrMinus ** plainValue).map { case (op, ex) =>
      UnOp(op, ex)
    }

    /** implementation #3 */
    val plainValueWithUnary3: Parser[Expr] = (unPlusOrMinus.opt ** plainValue).map {
      case (Some(op), ex) => UnOp(op, ex)
      case (None, ex)     => ex
    }

    /** non recursive sequence of +, - */
    def plainLowPri: Parser[Expr] =
      (plainValueWithUnary3 ** (biPlusOrMinus ** plainValueWithUnary3).many) map foldRA

    /** non recursive sequence of *, / */
    def plainMidPri: Parser[Expr] =
      (plainValueWithUnary3 ** (biMulOrDiv ** plainValueWithUnary3).many) map foldRA

    /** parsing binary operation - high priority */
    val biMulOrDiv: Parser[Op2] = mulOrDiv.map(Op2.unapply).map(_.get)

    /** parsing binary operation - low priority */
    val biPlusOrMinus: Parser[Op2] = plusOrMinus.map(Op2.unapply).map(_.get)

    /** parsing whatever between parens */
    def parens(p: Parser[Expr]): Parser[Expr] = surround(char('('), char(')'))(p)

    /** treat everything wrapped into braces as unit */
    def hiPri: Parser[Expr] = plainValueWithUnary3 | parens(lowPri)

    /** MP - recursive */
    def midPri: Parser[Expr] =
      (hiPri ** (biMulOrDiv ** hiPri).many) map foldRA

    /** LP - recursive */
    def lowPri: Parser[Expr] =
      (midPri ** (biPlusOrMinus ** midPri).many) map foldRA

    def wholeCombination: Parser[Expr] = root(lowPri)

  }

  import Parsers._
  import Domain._

  import scala.util.chaining.scalaUtilChainingOps
  val exprParser: Parser[Expr] = ExpressionParser.wholeCombination
  val parser: String => Either[ParseError, Expr] = ParserImpl.run(exprParser)
  def evaluate(ex: String) =
    ex
      .replaceAll("\\s", "")
      .pipe(parser)
      .map(evalNode)
      .fold(_ => ???, identity)

  def body(line: () => String) =
    line()
      .pipe(evaluate)
      .tap(println)

  def main(p: Array[String]): Unit = body { () => scala.io.StdIn.readLine }

//  def main(p: Array[String]): Unit = {
//    import EuclideanModulo._
//
//    val a: Int = -1402
//    val b: Int = 1000000007
//    val c: Int = a %% b
//    println(1000000007 + a)
//  }

//  def main(p: Array[String]): Unit = processFile("parser2.txt", body)
//
//  def processFile(name: String, process: (=> String) => Unit): Unit =
//    new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
//      .pipe { f =>
//        Using(scala.io.Source.fromFile(f)) { src =>
//          val it = src.getLines().map(_.trim)
//          Try(process(it.next()))
//            .recover { case x: Throwable => x.printStackTrace() }
//        }
//      }

}

object GCD {

  @tailrec
  def gcd(x: Int, y: Int): Int = {
    val r = x % y
    if (r == 0) y
    else gcd(y, r)
  }

}

class DivSpec extends AnyFunSpec with Matchers {

  import ExpressionsV2CombinedHR.Domain._

  describe("gcd") {
    import GCD._

    it("1a") {
      gcd(5, 7) shouldEqual 1
    }

    it("1b") {
      gcd(6, 12) shouldEqual 6
    }

    it("1c") {
      gcd(12, 6) shouldEqual 6
    }

    it("1d") {
      gcd(24, 36) shouldEqual 12
    }

    it("1e") {
      gcd(1053, 325) shouldEqual 13
    }
  }

  it("0") {
    inv(10) shouldEqual 700000005
  }

}

/** {{{
  *   55+3-(45*33)-25
  *   55+3-(1485-25)
  *   55+(3-1460)
  *   55+(-1457)
  *   -1402 mod P
  *   expected: 999998605
  * }}}
  */
