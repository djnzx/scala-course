package fp_red.red09

import fp_red.c_answers.c08testing._

import scala.language.implicitConversions
import scala.util.matching.Regex

case class Location(input: String, offset: Int = 0) {
  lazy val line = input.slice(0,offset+1).count(_ == '\n') + 1
  lazy val col = input.slice(0,offset+1).lastIndexOf('\n') match {
    case -1 => offset + 1
    case lineStart => offset - lineStart
  }
  def toError(msg: String): ParseError = ParseError(List((this, msg)))
}

// stack of parse errors
case class ParseError(stack: List[(Location,String)]) {
  def push(loc: Location, msg: String): ParseError = copy(stack = (loc,msg) :: stack)
}

// now, Parser[_], just a F[_], just a name
trait Parsers[Parser[+_]] { self =>

  /**
    * 1. Recognizes and returns a single Character
    */
  def char(c: Char): Parser[Char] = string(c.toString) map { _.charAt(0) }
  // we expect: char('a') which is run on the string("a") will return Right('a') 
  run(char('a'))('a'.toString) == Right('a')

  /**
    * 2. Recognizes and returns a single String
    * actually, lifts String to Parser 
    */
  implicit def string(s: String): Parser[String]

  // we expect
  run(string("abc"))("abc") == Right("abc")

  /**
    * 3. Chooses between two parsers, first attempting p1, and then p2 if p1 fails
    * 2-nd param must be lazy, because if 1st OK, we don't need to touch 2nd
    */
  def or[A](p1: Parser[A], p2: => Parser[A]): Parser[A]
  // we expect
  run(or(string("abra"),string("cadabra")))("abra") == Right("abra")
  run(or(string("abra"),string("cadabra")))("cadabra") == Right("cadabra")
  // laws
  val ab: Parser[String] = "a" | "b"
  val ba: Parser[String] = "b" | "a" // ???
  val aORb: Parser[String] = "a" or "b"
  val aORbORc1: Parser[String] = "a" | ("b" | "c")
  val aORbORc2: Parser[String] = ("a" | "b") | "c" // ???

  /**
    * 7. Applies the function f to the result of p, if successful
    *
    * law: map(p)(a => a) == p
    *
    * is no longer a primitive, can be expressed via flatMap
    */
  def map[A,B](pa: Parser[A])(f: A => B): Parser[B] = flatMap(pa) { f andThen succeed }
  def mapExplained[A,B](pa: Parser[A])(f: A => B): Parser[B] = flatMap(pa) { a: A =>
    val b: B = f(a)
    val pb: Parser[B] = succeed(b)
    pb
  }

  /**
    * 9. Always succeeds with the value a
    */
  def succeed[A](a: A): Parser[A] = string("") map { _ => a }
  // we expect
  run(succeed("whatever"))("any input") == Right("whatever")

  // now, we can write
  // many returns List[A]. we need to count
  val numA1: Parser[Int] = map(many(char('a')))(_.size)
  // by given syntax
  val numA2: Parser[Int] = char('a').many.map(_.size)
  // we expect
  run(numA1)("aaa") == Right(3)
  run(numA2)("b") == Right(0)

  /**
    * 10. Returns the portion of input inspected by p if successful
    * is going to be a primitive
    */
  def slice[A](p: Parser[A]): Parser[String]
  // we expect:
  run(slice(("a" | "b").many))("aaba") == Right("aaba")
  // we can write
  char('a').many.slice.map(_.length) // String.length is faster that List.size

  /**
    * 11. Sequences two parsers, running p1 and then p2, 
    * and returns the pair of their results if both succeed
    * product via flatMap
    * 2-nd param must be lazy!!!
    */
  def product[A, B](pa: Parser[A], pb: => Parser[B]): Parser[(A, B)] = for {
    a <- pa
    b <- pb
  } yield (a,b)
  // and we can write (product), because of implicit syntax class
  val pAB: Parser[(String, String)] = "a" ** "b"
  val pii: Parser[(Int, Int)] = char('a').many.slice.map(_.length) ** char('b').many1.slice.map(_.length)

  /**
    * map2 via product
    */
  def map2_product[A, B, C](pa: Parser[A], pb: => Parser[B])(f: (A, B) => C): Parser[C] = {
    val p: Parser[(A, B)] = product(pa, pb)
    val ft: ((A, B)) => C = f.tupled
    val pc: Parser[C] = map(p) { ft } // or with syntax: p map ft
    pc
  }
  /**
    * map2 via flatMap
    * 2-nd param must be lazy!!!
    */
  def map2[A, B, C](pa: Parser[A], pb: => Parser[B])(f: (A, B) => C): Parser[C] = for {
    a <- pa
    b <- pb
    c = f(a,b)
  } yield c

  trait Playground {
    def fabc[A,B,C](a: A, b: B): C
    def fabct[A,B,C](t: (A, B)): C = t match { case (a, b) => fabc(a, b) }
    def fabc2[A,B,C](a: A, b: B): C = fabct((a,b))

    fabc(1,2)
    fabct((1,2))
  }

  /** 5. more than 0 */
  def many[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p)) { _ :: _ } or succeed(Nil)
  /** 6. more than 1 */
  def many1[A](p: Parser[A]): Parser[List[A]] = map2(p, many(p)) { _ :: _ }

  /**
    * 4. Recognize repetitions
    */
  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] =
    if (n<=0) succeed(Nil)
    else map2(p, listOfN(n-1, p)) { _ :: _ }
  // we expect
  run(listOfN(3, "ab" | "cad"))("ababcad") == Right("ababcad")
  run(listOfN(3, "ab" | "cad"))("cadabab") == Right("cadabab")
  run(listOfN(3, "ab" | "cad"))("ababab") == Right("ababab")
  run(listOfN(3, "ab" | "cad"))("cadcadcad") == Right("ababab")

  // will make non-strict from strict
  def wrap[A](p: => Parser[A]): Parser[A]

  /**
    * context-sensitive primitive
    */
  def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B]

  /**
    * Recognizes a regular expression s
    */
  implicit def regex(r: Regex): Parser[String]
  // we will use it in error reporting

  def label[A](msg: String)(p: Parser[A]): Parser[A]
  def scope[A](msg: String)(p: Parser[A]): Parser[A]

  def errorLocation(e: ParseError): Location
  def errorMessage(e: ParseError): String

  def labelLaw[A](p: Parser[A], inputs: SGen[String]): Prop =
    Prop.forAll(inputs ** Gen.string) { case (input, msg) =>
      run(label(msg)(p))(input) match {
        case Left(e) => errorMessage(e) == msg
        case _ => true
      }
    }

  /** scope to support nesting */
  def scope[A](msg: String)(p: Parser[A]): Parser[A]

  val p: Parser[((String, List[String]), String)] = label("first magic word")("abra") **
    " ".many **
    label("second magic word")("cadabra")

  val spaces: Parser[List[String]] = " ".many

  val p1: Parser[((String, List[String]), String)] =
    scope("magic spell") {
      "abra" ** spaces ** "cadabra"
    }
  val p2: Parser[((String, List[String]), String)] =
    scope("gibberish") {
      "abba" ** spaces ** "babba"
    }
  val p3: Parser[((String, List[String]), String)] =
    p1 or p2

  // 9.5.3
  def attempt[A](p: Parser[A]): Parser[A]

  //    def fail: Parser[_]

  // we expect
  //    attempt(p flatMap (_ => fail)) or p2 == p2
  // we can write
  val p4 = (attempt("abra" ** spaces ** "abra") ** "cadabra") or ("abra" ** spaces ** "cadabra!")

  
  
  

  def run[A](p: Parser[A])(input: String): Either[ParseError, A]
  // attach syntax to the Parser
  implicit def syntaxForParser[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)

  /**
    * attach syntax for Parser
    * it works because of implicit def string(s: String): Parser[String]
    */
  implicit def stringToParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  /**
    * just syntax
    * everything just delegates to self instance
    */
  case class ParserOps[A](p: Parser[A]) {
    // OR strict
    def |[B>:A](p2: Parser[B]): Parser[B] = self.or(p, p2)
    // OR lazy
    def or[B>:A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
    def map[B](f: A => B): Parser[B] = self.map(p)(f)
    def flatMap[B](f: A => Parser[B]): Parser[B] = self.flatMap(p)(f)
    def many: Parser[List[A]] = self.many(p)
    def many1: Parser[List[A]] = self.many1(p)
    def slice: Parser[String] = self.slice(p)
    def **[B](pb: Parser[B]): Parser[(A, B)] = self.product(p, pb)
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
