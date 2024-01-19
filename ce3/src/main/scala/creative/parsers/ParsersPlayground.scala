package creative.parsers

import cats._
import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** - A method that goes from something that is not our algebra to our algebra is a `constructor`.
  *
  * - A combinator is any method that takes in our algebra as parameter (often the hidden `this` parameter) and returns our same algebra.
  *
  *  - An `interpreter` takes in our algebra and returns something of a different type.
  */
sealed trait Parser[A] {
  import Parser._

  def map[B](f: A => B): Parser[B] =
    ParserMap(this, f)

  def product[B](that: Parser[B]): Parser[(A, B)] =
    ParserProduct(this, that)

  def flatMap[B](f: A => Parser[B]): Parser[B] =
    ParserFlatMap(this, f)

  def orElse(that: Parser[A]): Parser[A] =
    ParserOrElse(this, that)

  def and(that: Parser[A])(implicit s: Semigroup[A]): Parser[A] =
    ParserAnd(this, that, s)

  def zeroOrMore(implicit m: Monoid[A]): Parser[A] =
    repeatAtLeast(0)

  def oneOrMore(implicit m: Monoid[A]): Parser[A] =
    repeatAtLeast(1)

  def repeatAtLeast(minimum: Int)(implicit m: Monoid[A]): Parser[A] = {
    def loop(count: Int): Parser[A] =
      if (count == 0) ParserRepeat(this, m)
      else ParserAnd(this, loop(count - 1), m)

    loop(minimum)
  }

  def repeatAtLeastA[Out](
      minimum: Int
    )(implicit
      accum: Accumulator[A, Out]
    ): Parser[Out] =
    ParserRepeatAccumulator(this, minimum, accum)

  def repeatBetween(min: Int, max: Int)(implicit m: Monoid[A]): Parser[A] =
    ParserRepeatBetween(this, min, max, m)

  def parse(input: String): Result[A] = {
    def loop[A](parser: Parser[A], index: Int): Result[A] =
      parser match {
        case ParserMap(source, f) =>
          loop(source, index).map(f)

        case ParserProduct(left, right) =>
          loop(left, index) match {
            case f: Failure            => f
            case Success(a, _, offset) =>
              loop(right, offset) match {
                case f: Failure            => f
                case Success(b, _, offset) =>
                  Success((a, b), input, offset)
              }
          }

        case ParserFlatMap(source, f) =>
          loop(source, index) match {
            case f: Failure                 => f
            case Success(result, _, offset) =>
              loop(f(result), offset)
          }

        case ParserOrElse(left, right) =>
          loop(left, index) match {
            case _: Failure    => loop(right, index)
            case s: Success[_] => s
          }

        case ParserAnd(left, right, m) =>
          loop(left, index) match {
            case f: Failure            => f
            case Success(x, _, offset) =>
              loop(right, offset) match {
                case f: Failure            => f
                case Success(y, _, offset) =>
                  Success(m.combine(x, y), input, offset)
              }
          }

        case ParserTailRecM(f, a) =>
          loop(f(a), index) match {
            case f: Failure                 => f
            case Success(result, _, offset) =>
              result match {
                case Left(a)  => loop(ParserTailRecM(f, a), offset)
                case Right(b) => Success(b, input, offset)
              }
          }

        case ParserRepeat(source, m) =>
          def repeatLoop(idx: Int, result: A): (Int, A) =
            loop(source, idx) match {
              case Failure(_, _, start)  => (start, result)
              case Success(a, _, offset) =>
                repeatLoop(offset, m.combine(result, a))
            }

          val (offset, a) = repeatLoop(index, m.empty)
          Success(a, input, offset)

        case ParserRepeatAccumulator(source, min, accum) =>
          def repeatLoop(
              count: Int,
              idx: Int,
              accumulated: accum.Accum
            ): Result[A] =
            loop(source, idx) match {
              case Failure(reason, input, start) =>
                if (count >= min)
                  Success(accum.retrieve(accumulated), input, start)
                else Failure(reason, input, start)
              case Success(result, _, offset)    =>
                repeatLoop(count + 1, offset, accum.append(accumulated, result))
            }

          repeatLoop(0, index, accum.create())

        case ParserRepeatBetween(source, min, max, m) =>
          def repeatLoop(count: Int, idx: Int, result: A): Option[(Int, A)] =
            if (count > max) (idx, result).some
            else
              loop(source, idx) match {
                case Failure(_, _, _)      =>
                  if (count >= min) (idx, result).some
                  else none
                case Success(a, _, offset) =>
                  repeatLoop(count + 1, offset, m.combine(result, a))
              }

          repeatLoop(0, index, m.empty) match {
            case None                =>
              Failure(
                s"Did not match between $min and $max times",
                input,
                index
              )
            case Some((idx, result)) => Success(result, input, idx)
          }

        case ParserChar(value) =>
          if (index >= input.size)
            Failure(
              "Input has ended but was expecting character ${value}",
              input,
              index
            )
          else if (input.charAt(index) == value)
            Success(value, input, index + 1)
          else
            Failure(
              s"Input did not contain character $value at index $index",
              input,
              index
            )

        case ParserCharWhere(predicate) =>
          if (index >= input.size)
            Failure(
              "Input has ended but was expecting a character",
              input,
              index
            )
          else {
            val ch = input.charAt(index)
            if (predicate(ch)) Success(ch, input, index + 1)
            else
              Failure(
                s"Input did not contain a character matching our predicate at index $index",
                input,
                index
              )
          }

        case ParserString(value) =>
          if (input.startsWith(value, index))
            Success(value, input, index + value.size)
          else
            Failure(
              s"Input did not start with $value at index $index",
              input,
              index
            )

        case ParserPure(value) => Success(value, input, index)

        case ParserFail() => Failure("This parser always fails", input, index)

        case ParserSucceed(m) => Success(m.empty, input, index)

        case ParserDelay(p)      => loop(p(), index)
        case p: ParserMemoize[A] => loop(p.force, index)
      }

    loop(this, 0)
  }
}
object Parser {
  def char(value: Char): Parser[Char]                            = ParserChar(value)
  def string(value: String): Parser[String]                      = ParserString(value)
  def pure[A](x: A): Parser[A]                                   = ParserPure(x)
  def fail[A]: Parser[A]                                         = ParserFail()
  def succeed[A](implicit m: Monoid[A]): Parser[A]               = ParserSucceed(m)
  def delay[A](parser: => Parser[A]): Parser[A]                  = ParserDelay(() => parser)
  def memoize[A](parser: => Parser[A]): Parser[A]                = ParserMemoize(() => parser)
  def charWhere(predicate: Char => Boolean): Parser[Char]        =
    ParserCharWhere(predicate)
  def charIn(char: Char, chars: Char*): Parser[Char]             =
    chars.foldLeft(Parser.char(char)) { (parser, char) =>
      parser.orElse(Parser.char(char))
    }
  def stringIn(string: String, strings: String*): Parser[String] =
    strings.foldLeft(Parser.string(string)) { (parser, string) =>
      parser.orElse(Parser.string(string))
    }

  final case class ParserChar(value: Char)                     extends Parser[Char]
  final case class ParserCharWhere(predicate: Char => Boolean) extends Parser[Char]
  final case class ParserString(value: String)                 extends Parser[String]
  final case class ParserPure[A](value: A)                     extends Parser[A]
  final case class ParserFail[A]()                             extends Parser[A]
  final case class ParserSucceed[A](monoid: Monoid[A])         extends Parser[A]

  final case class ParserMap[A, B](source: Parser[A], f: A => B)             extends Parser[B]
  final case class ParserProduct[A, B](left: Parser[A], right: Parser[B])    extends Parser[(A, B)]
  final case class ParserFlatMap[A, B](source: Parser[A], f: A => Parser[B]) extends Parser[B]
  final case class ParserOrElse[A](left: Parser[A], right: Parser[A])        extends Parser[A]
  final case class ParserAnd[A](
      left: Parser[A],
      right: Parser[A],
      semigroup: Semigroup[A])
      extends Parser[A]
  final case class ParserRepeat[A](source: Parser[A], monoid: Monoid[A])     extends Parser[A]
  final case class ParserRepeatAccumulator[In, Out](
      source: Parser[In],
      min: Int,
      accumulator: Accumulator[In, Out])
      extends Parser[Out]
  final case class ParserRepeatBetween[A](
      source: Parser[A],
      min: Int,
      max: Int,
      monoid: Monoid[A])
      extends Parser[A]
  final case class ParserTailRecM[A, B](f: A => Parser[Either[A, B]], a: A)  extends Parser[B]
  final case class ParserDelay[A](parser: () => Parser[A])                   extends Parser[A]
  final case class ParserMemoize[A](parser: () => Parser[A])                 extends Parser[A] {
    lazy val force: Parser[A] = parser()
  }

  implicit val parserMonadInstance: Monad[Parser] =
    new Monad[Parser] {
      // Override some methods we can implement directly

      override def map[A, B](fa: Parser[A])(f: A => B): Parser[B] =
        fa.map(f)

      override def product[A, B](fa: Parser[A], fb: Parser[B]): Parser[(A, B)] =
        fa.product(fb)

      override def ap[A, B](ff: Parser[A => B])(fa: Parser[A]): Parser[B] =
        ff.product(fa).map { case (f, a) => f(a) }

      def flatMap[A, B](fa: Parser[A])(f: A => Parser[B]): Parser[B] =
        fa.flatMap(f)

      def pure[A](x: A): Parser[A] = Parser.pure(x)

      def tailRecM[A, B](a: A)(f: A => Parser[Either[A, B]]): Parser[B] =
        ParserTailRecM(f, a)
    }
}

class ParsersPlaygroundSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Parser._

  test("string - success") {
    val r: Result[String] = Parser.string("Hello").parse("Hello there")
    pprint.pprintln(r)
    r shouldBe Success(result = "Hello", input = "Hello there", offset = 5)
  }

  test("string - failure") {
    val r: Result[String] = Parser.string("Hello").parse("Hell or Heaven")
    pprint.pprintln(r)
    r shouldBe Failure(reason = "Input did not start with Hello at index 0", input = "Hell or Heaven", start = 0)
  }

  test("map - success") {
    val r: Result[Int] = Parser.string("123").map(_.toInt).parse("123tail")
    pprint.pprintln(r)
    r shouldBe Success(result = 123, input = "123tail", offset = 3)
  }

  test("alt - success") {
    val oneDigit: Parser[Int] =
      List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        .foldLeft(Parser.fail[Int]) { (pa, digit) =>
          pa.orElse(Parser.string(digit.toString).as(digit))
        }

    val r = oneDigit.parse("12345")
    pprint.pprintln(r)
    r shouldBe Success(result = 1, input = "12345", offset = 1)
  }

}
