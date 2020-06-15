package fp_red.red09

import fp_red.red09.ReferenceTypes.Parser
import org.scalatest._
  
class ParsersReferenceSpec extends funspec.AnyFunSpec
  with matchers.should.Matchers {
  val R = Reference
  import R._

  describe("Parsers Reference Implementation:") {
    describe("1. succeed: with given value, doesn't touch the pointer") {
      it("succeed: type: Int") {
        R.runLen(R.succeed(1))("abcxyz") shouldBe
          Right(1, 0)
      }
      it("succeed: type: String") {
        R.runLen(R.succeed("ABC"))("abcxyz") shouldBe
          Right("ABC", 0)
      }
    }
    
    describe("2. fail: with given message, doesn't touch the pointer") {
      it("fail:") {
        R.runLen(R.fail("WRONG!"))("abcxyz") shouldBe
          Left(ParseError(List(
            (Location("abcxyz", 0), "WRONG!")
          )))
      }
    }
    
    describe("3. string:") {
      it("exact match") {
        R.runLen(string("abc"))("abc") shouldBe
          Right("abc", 3)
      }
      it("starts with") {
        R.runLen(string("abc"))("abc1") shouldBe
          Right("abc", 3)
      }
      it("doesn't start with") {
        R.run(string("abc"))("ab")
          .isLeft shouldBe true
      }
      it("doesn't start with: (prefix + content)") {
        R.run(string("abc"))("1abc")
          .isLeft shouldBe true
      }

    }
    
    describe("4. or combinator:") {
      it("or:1") {
        R.runLen(string("ab") | string("cde"))("abXYZ") shouldBe
          Right("ab", 2)
      }
      it("or:2") {
        R.runLen(string("ab") | string("cde"))("cdeXYZ") shouldBe
          Right("cde", 3)
      }
      it("or:3") {
        R.runLen(string("ab") | string("cde"))("_ab")
          .isLeft shouldBe true
      }
    }
    
    describe("5. flatMap combinator") {
      val seq: Parser[(String, String)] = for {
        a <- string("abc")
        b <- string("123")
      } yield (a, b)

      it("flatMap:T:T") {
        R.runLen(seq)("abc123x") shouldBe
          Right((("abc", "123"), 6))
      }
      it("flatMap:T:F") {
        R.runLen(seq)("abc12_") shouldBe
          Left(ParseError(List((Location("abc12_", 5), "'123'"))))
      }
      it("flatMap:F:T") {
        R.runLen(seq)("abX123") shouldBe
          Left(ParseError(List((Location("abX123", 2), "'abc'"))))
      }
    }
    
    describe("6. regex") {
      it("regex: valid") {
        R.runLen(R.regex("\\d+".r))("1234") shouldBe
          Right(("1234", 4))
      }
      it("regex: invalid") {
        R.runLen(R.regex("\\d+".r))("abc123") shouldBe
          Left(ParseError(List((Location("abc123", 0), "regex \\d+"))))
      }
    }
    
    describe("7. scope") {
      
    }
    
    describe("8. label") {
      
    }
    
    describe("9. attempt") {
      
    }
    
    describe("10. slice") {
      
    }
    
  }
  
}
