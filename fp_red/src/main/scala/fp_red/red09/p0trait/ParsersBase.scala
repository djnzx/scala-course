package fp_red.red09.p0trait

import scala.util.matching.Regex

trait ParsersBase[Parser[+_]] {
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

}
