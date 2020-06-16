package c19parse

import org.scalatest._
import fastparse._, NoWhitespace._

class Parse191Spec extends funspec.AnyFunSpec {

  describe("recursion with fastparse") {
    it("1:") {
      sealed trait Phrase
      case class Word(s: String) extends Phrase
      case class Pair(lhs: Phrase, rhs: Phrase) extends Phrase
      
      def prefix[_: P] = P( "a" | "b" ).!.map(Word)
      def suffix[_: P] = P( "c" | "d" ).!.map(Word)
      def ws[_: P] = P( " ".rep(1) )
      
      def parened[_: P] = P( "(" ~ parser ~ ")" )
      def parser[_: P]: P[Phrase] = P( (parened | prefix) ~ ws ~ (parened | suffix) ).map {
        case (lhs, rhs) => Pair(lhs, rhs)
      }
      val r = fastparse.parse("(a c) ((b d) c)", parser(_))
      pprint.log(r)
    }
  }
  
}
