package c19parse

import fastparse._, NoWhitespace._, SingleLineWhitespace._ 
import MultiLineWhitespace._
import ScriptWhitespace._
import JavaWhitespace._ ,ScalaWhitespace._

object CalcParseEval {
  
  /** do One operation */
  def doOp(op: String, n1: Int, n2: Int): Int = op match {
    case "+" => n1 + n2
    case "-" => n1 - n2
    case "*" => n1 * n2
    case "/" => n1 / n2
  }
  /** 
    * just evaluate
    * t._1 n1
    * t._2 Seq(op, n2)
    */
  def eval(t: (Int, Seq[(String, Int)])): Int = t match {
    case (n, Nil) => n
    case (a, l) => l.foldLeft(a) { case (acc, (op, x)) => doOp(op, acc, x) }
  }
  /** extract number */
  def number[_: P]: P[Int] = P( CharIn("0-9").rep(1).!.map(_.toInt) )
  /** recursive grammar */
  def parens[_: P]: P[Int] = P( "(" ~/ addSub ~ ")" )
  def factor[_: P]: P[Int] = P( number | parens )
  def divMul[_: P]: P[Int] = P( factor ~ (CharIn("*/").! ~/ factor).rep ).map(eval)
  def addSub[_: P]: P[Int] = P( divMul ~ (CharIn("+\\-").! ~/ divMul).rep ).map(eval)
  /** root of grammar */
  def expr[_: P]: P[Int]   = P( addSub ~ End )
  
}
