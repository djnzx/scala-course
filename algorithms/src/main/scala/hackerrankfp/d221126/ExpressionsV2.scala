package hackerrankfp.d221126

import scala.util.Try
import scala.util.Using

/** https://www.hackerrank.com/challenges/expressions-v2/problem */
object ExpressionsV2 {

  import hackerrankfp.d200612_10.Parsers._
  import Domain._

//  case class NP(n: Int, p: Int)
//
//  object MonomParser {
//    import ParserImpl.number
//    import ParserImpl.char
//    import ParserImpl.attempt
//    import ParserImpl.succeed
//    import ParserImpl.syntaxForParser
//
//    val n: Parser[Int] = number
//    val x: Parser[Char] = char('x')
//    val p: Parser[Int] = char('^') *> number
//    val nxp: Parser[NP] = (n <* x) ** p map { case (n, p) => NP(n, p) }
//    val nx1: Parser[NP] = n <* x map { NP(_, 1) }
//    val n_ : Parser[NP] = n map { NP(_, 0) }
//    val xp: Parser[NP] = x *> p map { NP(1, _) }
//    val x_ : Parser[NP] = x *> succeed(NP(1, 1))
//    val monom: Parser[NP] = attempt(nxp) | attempt(nx1) | attempt(n_) | attempt(xp) | attempt(x_)
//  }
//
//  object MathOpToPolynomParser {
//    import ParserImpl._
//
//    def process(t: (Expr[Polynom], Seq[(Char, Expr[Polynom])])): Expr[Polynom] = t match {
//      case (n, Nil) => n
//      case (a, l)   => l.foldLeft(a) { case (acc, (op, x)) => BinOp(op, acc, x) }
//    }
//    val plusOrMinus: Parser[Char] = char('+') | char('-')
//    val mulOrDiv: Parser[Char] = char('*') | char('/')
//    def value: Parser[Expr[Polynom]] = MonomParser.monom.map { case NP(n, p) => Value(Monom(n, p).toPolynom) }
//    def parens: Parser[Expr[Polynom]] = surround(char('('), char(')'))(addSub)
//    def block: Parser[Expr[Polynom]] = value | parens
//    def divMulExplicit: Parser[Expr[Polynom]] = (block ** (mulOrDiv ** block).many).map(process)
//    def mulImplicit: Parser[Expr[Polynom]] = (value ** block).map { case (x1, x2) => process(x1, List(('*', x2))) }
//    def divMul: Parser[Expr[Polynom]] = attempt(mulImplicit) | divMulExplicit
//    def addSub: Parser[Expr[Polynom]] = (divMul ** (plusOrMinus ** divMul).many).map(process)
//    def wholeCombination: Parser[Expr[Polynom]] = root(addSub)
//  }
//
//
  import scala.util.chaining.scalaUtilChainingOps
//  val exprParser: Parser[Expr[_]] = MathOpToPolynomParser.wholeCombination
//  val parser: String => Either[ParseError, Expr[Polynom]] = ParserImpl.run(exprParser)
  def simplify(ex: String) = {
    ex
      .replaceAll("\\s", "")
//      .pipe(parser)
//      .map(evalNode)
//      .map(_.toStringHR)
//      .fold(_ => ???, identity)
  }

  def body(line: => String): Unit = {
    val N = line.toInt
    (1 to N).map(_ => line).map(simplify).foreach(println)
  }

  //
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }

  def main(p: Array[String]): Unit = processFile("parser2.txt", body)

  def processFile(name: String, process: (=> String) => Unit): Unit =
    new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
      .pipe { f =>
        Using(scala.io.Source.fromFile(f)) { src =>
          val it = src.getLines().map(_.trim)
          Try(process(it.next()))
            .recover { case x: Throwable => x.printStackTrace() }
        }
      }

}
