package c19parse

import org.scalatest._
import fastparse._
import NoWhitespace._
import fastparse.Parsed.Success

/**
  * https://www.lihaoyi.com/fastparse/#Basic
  */
class Parse191Spec extends funspec.AnyFunSpec
  with matchers.should.Matchers {

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
  
  describe("parse:calculator") {
    describe("eval") {
      import c19parse.CalcParseEval.expr

      it("1:") {
        parse("10+11", expr(_)) shouldBe Success(21, 5)
      }
      it("2:") {
        parse("(6+1*2)+3*4", expr(_)) shouldBe Success(20, 11)
      }
      it("3:") {
        parse("((4+1*2)+(3*4+3*5))/3", expr(_)) shouldBe Success(11, 21)
      }
    }

    describe("toStructure") {
      import c19parse.CalcParseToStructure.{build, BiOp, Value}

      it("1:") {
        parse("10+11", build(_)) shouldBe
          Success(BiOp("+", Value(10), Value(11)), 5)
      }

      it("2:") {
        parse("10+11*3", build(_)) shouldBe
          Success(BiOp("+", Value(10), BiOp("*", Value(11), Value(3))), 7)
      }

      it("3:") {
        parse("(10+11*3)/2", build(_)) shouldBe
          Success(BiOp("/", BiOp("+", Value(10), BiOp("*", Value(11), Value(3))), Value(2)), 11)
      }
    }
    
  }
  
}
