package fp_red.red09

object Impl002 {

  type Parser[+A] = Location => Result[A]

  sealed trait Result[+A] {
    def mapError(f: ParseError => ParseError): Result[A] = this match {
      case Failure(e) => Failure(f(e))
      case _ => this
    }
  }
  // we can use charsConsumed to update Location further
  case class Success[+A](get: A, charsConsumed: Int) extends Result[A]
  case class Failure(get: ParseError) extends Result[Nothing]
  
  trait Idea002 extends Parsers[Parser] {
    override def scope[A](msg: String)(p: Parser[A]): Parser[A] =
      (s: Location) => p(s).mapError(_.push(s, msg))
    // 9.6.3
  }
  
}
