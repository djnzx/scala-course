package fp_red.red09

import java.util.regex.Pattern

import fp_red.c_answers.c08testing._

import scala.language.implicitConversions
import scala.util.matching.Regex

/** case class to handle input data
  * contains whole input
  * and current position
  */
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

/**
  * contains all errors
  * in the List[(Location,String)]
  */
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

trait Parsers[Parser[+_]] { self =>
  /**
    * 0. runner + extractor
    * the idea - how we want to use it
    * ABSTRACT
    */
  def run[A](p: Parser[A])(input: String): Either[ParseError, A]

  /**
    * 1. whatever given -
    * just SUCCEEDS with a given value
    * w/o moving the pointer
    * (consume no characters)
    * ABSTRACT
    */
  def succeed[A](a: A): Parser[A]

  /**
    * 2. whatever given -
    * just FAILS with a given message
    * w/o moving the pointer
    * (consume no characters)
    * ABSTRACT
    */
  def fail[A](msg: String): Parser[A]

  /**
    * 3. Recognizes and returns a single String
    * actually, just String.startsWth(s)
    * + move the pointer to the next location
    * ABSTRACT
    */
  implicit def string(s: String): Parser[String]

  /**
    * 4. try to apply 1st parser
    * and if it fails on the Zeroth char
    * we can try second one
    * 2-nd param must be lazy, because if 1st OK, we don't need to touch 2nd
    * ABSTRACT
    */
  def or[A](p1: Parser[A], p2: => Parser[A]): Parser[A]

  /**
    * 5. context-sensitive primitive
    * chaining, based on previous value
    * ABSTRACT
    */
  def flatMap[A, B](pa: Parser[A])(g: A => Parser[B]): Parser[B]

  /**
    * 6. Recognizes a regular expression as a Parser
    * all-or-nothing
    * ABSTRACT
    */
  implicit def regex(r: Regex): Parser[String]

  /**
    * 7. scope to support nesting
    * result manipulation.
    * actually, just a wrapper
    * doesn't do any parsing
    * ABSTRACT
    */
  def scope[A](msg: String)(p: Parser[A]): Parser[A]

  /**
    * 8. label errors
    * result manipulation.
    * actually, just a wrapper
    * doesn't do any parsing
    * ABSTRACT
    */
  def label[A](msg: String)(p: Parser[A]): Parser[A]

  /**
    * 9. attempt
    * result manipulation.
    * actually, just a wrapper
    * doesn't do any parsing
    * ABSTRACT
    * 9.5.3
    */
  def attempt[A](p: Parser[A]): Parser[A]

  /**
    * 10. slice
    * Returns the portion of input inspected by p
    * if p was successful
    * ABSTRACT
    */
  def slice[A](p: Parser[A]): Parser[String]

  /**
    * Attach syntax to the Parser
    */
  implicit def syntaxForParser[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)
  /**
    * Attach syntax to everything
    * what can be lifted to the Parser
    * it works because of implicit def string(s: String): Parser[String]
    */
  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  /** -----------------------------------
    * everything else is non-primitive
    * and can be expressed via primitives
    * -----------------------------------
    */

  /**
    * 11. Recognizes and returns a single Character
    */
  def char(c: Char): Parser[Char] = string(c.toString) map { _.charAt(0) }

  /**
    * 12. 0 or more repetition of the parser (parsers combination)
    */
  def many[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p)) { _ :: _ } or succeed(Nil)

  /**
    * 13. 1 or more repetition of the parser (parsers combination)
    */
  def many1[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p)) { _ :: _ }

  /**
    * 14. Recognize repetitions
    * for N times
    */
  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] =
    if (n<=0) succeed(Nil)
    else map2(p, listOfN(n-1, p)) { _ :: _ }

  /**
    * 15. Sequences two parsers, running p1 and then p2,
    * and returns the pair of their results if both succeed
    * product via flatMap
    * 2-nd param must be lazy!!!
    */
  def product[A, B](pa: Parser[A], pb: => Parser[B]): Parser[(A, B)] = for {
    a <- pa
    b <- pb
  } yield (a, b)

  /**
    * 16A. map2 via flatMap
    * 2-nd param must be lazy!!!
    */
  def map2[A, B, C](pa: Parser[A], pb: => Parser[B])(f: (A, B) => C): Parser[C] = for {
    a <- pa
    b <- pb
    c = f(a,b)
  } yield c

  /**
    * 16B. map2 via product
    * 2-nd param must be lazy!!!
    */
  def map2_product[A, B, C](pa: Parser[A], pb: => Parser[B])(f: (A, B) => C): Parser[C] = {
    val p: Parser[(A, B)] = product(pa, pb)
    val ft: ((A, B)) => C = f.tupled
    val pc: Parser[C] = map(p) { ft } // or with syntax: p map ft
    pc
  }

  /**
    * 17. map via flatMap
    * Applies the function f to the result of p, if successful
    * law: map(p)(a => a) == p
    */
  def map[A,B](pa: Parser[A])(f: A => B): Parser[B] = flatMap(pa) { f andThen succeed }
  def mapExplained[A,B](pa: Parser[A])(f: A => B): Parser[B] = flatMap(pa) { a: A =>
    val b: B = f(a)
    val pb: Parser[B] = succeed(b)
    pb
  }

  /**
    * 18. Sequences two parsers, ignoring the result of the first.
    * We wrap the ignored half in slice, since we don't care about its result. */
  def skipL[B](p: Parser[Any], p2: => Parser[B]): Parser[B] =
    map2(slice(p), p2)((_,b) => b)

  /**
    * 19. Sequences two parsers, ignoring the result of the second.
    * We wrap the ignored half in slice, since we don't care about its result. */
  def skipR[A](p: Parser[A], p2: => Parser[Any]): Parser[A] =
    map2(p, slice(p2))((a,_) => a)

  /**
    * 20. if parsed - map to Some(val)
    * else None
    */
  def opt[A](p: Parser[A]): Parser[Option[A]] =
    p.map(Some(_)) or succeed(None)

  /**
    * 21.consumes zero or more whitespace characters.
    */
  def whitespace: Parser[String] = "\\s*".r

  /**
    * 22. consumes 1 or more digits.
    */
  def digits: Parser[String] = "\\d+".r

  /**
    * 23. consumes reluctantly until it encounters the given string.
    */
  def thru(s: String): Parser[String] = (".*?"+Pattern.quote(s)).r

  /**
    * 24. Unescaped string literals, like "foo" or "bar".
    */
  def quoted: Parser[String] = string("\"") *> thru("\"").map(_.dropRight(1))

  /**
    * 25. Unescaped or escaped string literals, like
    * "An \n important \"Quotation\"" or "bar".
    */
  def escapedQuoted: Parser[String] =
  // rather annoying to write, left as an exercise
  // we'll just use quoted (unescaped literals) for now
    token(quoted label "string literal")

  /**
    * 26. Integer number as String with + or - before
    */
  def intString: Parser[String] =
    token("[-+]?[0-9]+".r)

  /**
    * 26A. Integer number as String without + or - before
    */
  def intStringWoSign: Parser[String] =
    token("[0-9]+".r)

  /**
    * 27. Integer number as Integer
    */
  def integer: Parser[Int] =
    intString map { _.toInt } label "integer literal"

  /**
    * 27A. Non Neg Int
    */
  def integerWoSign: Parser[Int] =
    intStringWoSign map { _.toInt } label "integer w/o sign literal"

  /**
    * 27A. Long number as Long
    */
  def long: Parser[Long] =
    intString map { _.toLong } label "long literal"

  /**
    * 28. C/Java style floating point literals, e.g .1, -1.0, 1e9, 1E-23, etc.
    * Result is left as a string to keep full precision
    */
  def doubleString: Parser[String] =
    token("[-+]?([0-9]*\\.)?[0-9]+([eE][-+]?[0-9]+)?".r)

  /**
    * 29. Floating point literals, converted to a `Double`.
    */
  def double: Parser[Double] =
    doubleString map { _.toDouble } label "double literal"

  /**
    * 30. Attempts `p` and strips trailing whitespace,
    * usually used for the tokens of a grammar.
    */
  def token[A](p: Parser[A]): Parser[A] =
    attempt(p) <* whitespace

  /**
    * 31. Zero or more repetitions of `p`,
    * separated by `p2`,whose results are ignored.
    * `Parser[Any]` since don't care about result type of separator
    */
  def sep[A](p: Parser[A], p2: Parser[Any]): Parser[List[A]] =
    sep1(p, p2) or succeed(List())

  /**
    * 32. One or more repetitions of `p`,
    * separated by `p2`, whose results are ignored.
    */
  def sep1[A](p: Parser[A], p2: Parser[Any]): Parser[List[A]] =
    map2(p, many(p2 *> p))(_ :: _)

  /**
    * 33.Parses a sequence of left-associative binary
    * operators with the same precedence.
    */
  def opL[A](p: Parser[A])(op: Parser[(A,A) => A]): Parser[A] =
    map2(p, many(op ** p))((h,t) => t.foldLeft(h)((a,b) => b._1(a,b._2)))

  /**
    * 34. Wraps `p` in start/stop delimiters.
    */
  def surround[A](start: Parser[Any], stop: Parser[Any])(p: => Parser[A]) =
    start *> p <* stop

  /**
    * 35. A parser that succeeds when given empty input.
    */
  def eof: Parser[String] =
    regex("\\z".r).label("unexpected trailing characters")

  /**
    * 36. The root of the grammar, expects no further input following `p`.
    */
  def root[A](p: Parser[A]): Parser[A] =
    p <* eof

  /**
    * just syntax
    * everything just delegates to self instance
    */
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

  /**
    * we will use it during testing phase
    */
  object Laws {
    def equal[A](p1: Parser[A], p2: Parser[A])(in: Gen[String]): Prop =
      Prop.forAll(in) { s => run(p1)(s) == run(p2)(s) }
    def mapLaw[A](p: Parser[A])(in: Gen[String]): Prop =
      equal(p.map { identity }, p)(in)
  }

}
