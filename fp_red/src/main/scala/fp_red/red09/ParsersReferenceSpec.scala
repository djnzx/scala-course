package fp_red.red09

import fp_red.red09.ReferenceTypes.Parser
import org.scalatest._

class ParsersReferenceSpec extends funspec.AnyFunSpec
  with matchers.should.Matchers {
  val R = Reference

  import R._

  describe("Parsers Reference Implementation (primitives)") {
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
        R.runLen("abc")("abc") shouldBe
          Right("abc", 3)
      }
      it("starts with") {
        R.runLen("abc")("abc1") shouldBe
          Right("abc", 3)
      }
      it("doesn't start with") {
        R.run("abc")("ab") shouldBe
          Left(ParseError(List((Location("ab", 2), "'abc'"))))
      }
      it("doesn't start with: (prefix + content)") {
        R.run("abc")("1abc") shouldBe
          Left(ParseError(List((Location("1abc", 0), "'abc'"))))
      }

    }

    describe("4. or combinator:") {
      it("or:1") {
        R.runLen("ab" | "cde")("abXYZ") shouldBe
          Right("ab", 2)
      }
      it("or:2") {
        R.runLen("ab" | "cde")("cdeXYZ") shouldBe
          Right("cde", 3)
      }
      it("or:3") {
        R.runLen("ab" | "cde")("_ab") shouldBe
          Left(ParseError(List((Location("_ab", 0), "'cde'"))))
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
      it("slice:1") {
        R.runLen(digits)("123") shouldBe
          Right(("123", 3))
      }
      it("slice:2") {
        R.runLen(digits)("123abc") shouldBe
          Right(("123", 3))
      }
      it("slice:3") {
        // TODO fix many for string containing only that char 
        R.runLen(char('a').many.slice.map(_.length))("aaaaa_") shouldBe
          Right((5, 5))
      }
      it("slice:4") {
        // TODO fix many for string containing only that char 
        R.runLen(slice(("a" | "b").many))("aaba_") shouldBe
          Right(("aaba", 4))
      }
    }

  }

  describe("Parsers Reference Implementation (non-primitives)") {
    describe("11. char") {
      it("char:1") {
        R.runLen(char('a'))("a") shouldBe
          Right(('a', 1))
      }
      it("char:2") {
        R.runLen(char('a'))("abc") shouldBe
          Right(('a', 1))
      }
    }
    
    describe("12. many") {
      it("many: none:on empty") {
        R.runLen(char('^').many)("") shouldBe
          Right((List(), 0))
      }
      it("many: none:on non-empty") {
        R.runLen(char('^').many)("abc") shouldBe
          Right((List(), 0))
      }
      // TODO fix many for string containing only that char 
      it("many: 1:1") {
          R.runLen(char('^').many)("^_")
        Right((List('^'), 1))
      }
      it("many: 1:N") {
          R.runLen(char('^').many)("^abc")
        Right((List('^'), 1))
      }
      // TODO fix many for string containing only that char 
      it("many: N:N") {
        R.runLen(char('^').many)("^^^_") shouldBe
          Right((List('^', '^', '^'), 3))
      }
      it("many: N:>N") {
        R.runLen(char('^').many)("^^abc") shouldBe
          Right((List('^', '^'), 2))
      }
    }

    describe("13. many1") {
      // TODO: it crashes when reaches the end of the string
      it("many1:0") {
        R.run("12".many1)("_") shouldBe
          Left(ParseError(List((Location("_", 0), "'12'"))))
      }
      it("many1:1") {
        R.run("12".many1)("12_") shouldBe
          Right(List("12"))
      }
      it("many1:2") {
        R.run("12".many1)("1212_") shouldBe
          Right(List("12", "12"))
      }
    }
    
    describe("14. listOfN") {
      it("listOfN:4a") {
        R.run(listOfN(4, "ab" | "cd" | "ef" | "gh"))("abcdefgh") shouldBe
          Right(List("ab", "cd", "ef", "gh"))
      }
      it("listOfN:4b") {
        R.run(listOfN(4, "ab" | "cd" | "ef" | "gh"))("abefghcd") shouldBe
          Right(List("ab", "ef", "gh", "cd"))
      }
      it("listOfN:4c") {
        R.run(listOfN(4, "ab" | "cd" | "ef" | "gh"))("abghcdef") shouldBe
          Right(List("ab", "gh", "cd", "ef"))
      }
    }
  }

  describe("Monom") {
    val xp = char('x') ** (char('^') *> intString).many.map {
      case Nil => 1
      case h :: Nil => h.toInt
    }
    it("many: x") {
      R.run(xp)("x_") shouldBe Right(('x', 1))
    }
    it("many: x^1") {
      R.run(xp)("x^1_") shouldBe Right(('x', 1))
    }
    it("many: x^-1") {
      R.run(xp)("x^-1_") shouldBe Right(('x', -1))
    }
    it("many: x^2") {
      R.run(xp)("x^2_") shouldBe Right(('x', 2))
    }
    it("many: x^20") {
      R.run(xp)("x^20_") shouldBe Right(('x', 20))
    }
    it("many: x^-23") {
      R.run(xp)("x^-23_") shouldBe Right(('x', -23))
    }

  }

}