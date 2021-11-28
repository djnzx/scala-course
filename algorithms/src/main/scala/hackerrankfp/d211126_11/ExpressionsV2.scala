package hackerrankfp.d211126_11

import scala.util.Try
import scala.util.Using

/** https://www.hackerrank.com/challenges/expressions-v2/problem */
object ExpressionsV2 {

  import hackerrankfp.d200612_10.Parsers._
  import Domain._
  import Evaluation.evalNode

  import scala.util.chaining.scalaUtilChainingOps
  val exprParser: Parser[Expr] = ExpressionParser.wholeCombination
  val parser: String => Either[ParseError, Expr] = ParserImpl.run(exprParser)
  def evaluate(ex: String) =
    ex
      .replaceAll("\\s", "")
      .pipe(parser)
      .map(evalNode)
      .fold(_ => ???, identity)

  def body(line: => String) =
    (1 to line.toInt)
      .map(_ => line)
      .map(evaluate)
      .foreach(println)

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
