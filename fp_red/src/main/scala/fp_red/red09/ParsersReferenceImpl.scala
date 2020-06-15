package fp_red.red09

import fp_red.red09.ReferenceTypes._

import scala.util.matching.Regex

/**
  * case classes
  * for representation
  */
object ReferenceTypes {

  /** A parser is a kind of state action that can fail. */
  type Parser[+A] = ParseState => Result[A]

  /** `ParseState` wraps a `Location` and provides some extra
    * convenience functions. The sliceable parsers defined
    * in `Sliceable.scala` add an `isSliced` `Boolean` flag
    * to `ParseState`.
    */
  case class ParseState(loc: Location) {
    def advanceBy(numChars: Int): ParseState =
      copy(loc = loc.copy(offset = loc.offset + numChars))
    def input: String = loc.input.substring(loc.offset)
    def slice(n: Int) = loc.input.substring(loc.offset, loc.offset + n)
  }

  /* Likewise, we define a few helper functions on `Result`. */
  sealed trait Result[+A] {
    def extract: Either[ParseError,A] = this match {
      case Failure(e,_) => Left(e)
      case Success(a,_) => Right(a)
    }
    def extractLen: Either[ParseError,(A, Int)] = this match {
      case Failure(e,_) => Left(e)
      case Success(a, l) => Right((a, l))
    }
    /* Used by `attempt`. */
    def uncommit: Result[A] = this match {
      case Failure(e, true) => Failure(e, isCommitted = false)
      case _ => this
    }
    /* Used by `flatMap` */
    def addCommit(isCommitted: Boolean): Result[A] = this match {
      case Failure(e,c) => Failure(e, c || isCommitted)
      case _ => this
    }
    /* Used by `scope`, `label`. */
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
  case class Failure(get: ParseError,
                     // false - error on the 0 char - we can try another
                     // true - error in the middle - we can't try another
                     isCommitted: Boolean) extends Result[Nothing]

  /** Returns -1 if s1.startsWith(s2), otherwise returns the
    * first index where the two strings differed. If s2 is
    * longer than s1, returns s1.length. */
  def firstNonmatchingIndex(s1: String, s2: String, offset: Int): Int = {
    var i = 0
    while (i < s1.length && i < s2.length) {
      if (s1.charAt(i+offset) != s2.charAt(i)) return i
      i += 1
    }
    if (s1.length-offset >= s2.length) -1
    else s1.length-offset
  }
}

object Reference extends Parsers[Parser] {

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
  def string(w: String): Parser[String] = s =>
    firstNonmatchingIndex(s.loc.input, w, s.loc.offset) match {
      case -1 => Success(w, w.length) // they matched
      case i  => Failure(s.loc.advanceBy(i).toError(s"'$w'"), i != 0)
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








  /* overridden version of `many` that accumulates
   * the list of results using a monolithic loop. This avoids
   * stack overflow errors for most grammars.
   */
  override def many[A](p: Parser[A]): Parser[List[A]] =
    s => {
      val buf = new collection.mutable.ListBuffer[A]
      def go(p: Parser[A], offset: Int): Result[List[A]] = {
        p(s.advanceBy(offset)) match {
          case Success(a,n)         => buf += a; go(p, offset+n)
          case f @ Failure(_, true) => f
          case     Failure(_, _)    => Success(buf.toList,offset)
        }
      }
      go(p, 0)
    }
}

