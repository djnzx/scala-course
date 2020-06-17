package fp_red.red09

import fp_red.red09.Algebraic.MathOperationMonomParser.Value
import fp_red.red09.ReferenceTypes.{ParseState, Parser}
import fp_red.red09.algebra.SimplifyAlgebraicExpressions.Monom
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
        R.runLen(char('a').many.slice.map(_.length))("aaaaa") shouldBe
          Right((5, 5))
      }
      it("slice:4") {
        R.runLen(slice(("a" | "b").many))("aaba") shouldBe
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
      it("many: 1:1") {
          R.runLen(char('^').many)("^")
        Right((List('^'), 1))
      }
      it("many: 1:N") {
          R.runLen(char('^').many)("^abc")
        Right((List('^'), 1))
      }
      it("many: N:N") {
        R.runLen(char('^').many)("^^^") shouldBe
          Right((List('^', '^', '^'), 3))
      }
      it("many: N:>N") {
        R.runLen(char('^').many)("^^abc") shouldBe
          Right((List('^', '^'), 2))
      }
    }

    describe("13. many1") {
      it("many1:0") {
        R.run("12".many1)("") shouldBe
          Left(ParseError(List((Location("", 0), "'12'"))))
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

  /**
    * Monom parser from the task
    * https://www.hackerrank.com/challenges/simplify-the-algebraic-expressions/problem
    */
  describe("Monom") {
    
    import Algebraic.MonomParser._
    import Algebraic.MathOperationMonomParser.build
    
    describe("x^p") {
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
    
    describe("n*x") {
      val oi = opt(integer)
      it("opt:1") {
        R.run(oi)("x") shouldBe Right(None)
      }
      it("opt:2") {
        R.run(oi)("-x") shouldBe Right(None)
      }
      it("opt:3") {
        R.run(oi)("2x") shouldBe Right(Some(2))
      }
      it("opt:4") {
        R.run(oi)("-3x") shouldBe Right(Some(-3))
      }
    }
    
    describe("nx^p") {
      
      describe("1 by 1 w.o combinations") {
        it("nx^p: 3x^5") {
          R.run(nxp)("3x^5") shouldBe Right(NP(3, 5))
        }
        it("nx^p: 4x") {
          R.run(nx1)("4x") shouldBe Right(NP(4, 1))
        }
        it("nx^p: 6") {
          R.run(n_)("6") shouldBe Right(NP(6, 0))
        }
        it("nx^p: x^2") {
          R.run(xp)("x^2") shouldBe Right(NP(1, 2))
        }
        it("nx^p: x") {
          R.run(x_)("x") shouldBe Right(NP(1, 1))
        }
      }
      describe("combinations...") {
        it("nx^p: 3x^5") {
          R.run(monom)("3x^5") shouldBe Right(NP(3, 5))
        }
        it("nx^p: 4x") {
          R.run(monom)("4x") shouldBe Right(NP(4, 1))
        }
        it("nx^p: 6") {
          R.run(monom)("6") shouldBe Right(NP(6, 0))
        }
        it("nx^p: x^2") {
          R.run(monom)("x^2") shouldBe Right(NP(1, 2))
        }
        it("nx^p: x_") {
          R.run(monom)("x") shouldBe Right(NP(1, 1))
        }
      }
    }

    describe("nx^p: real cases") {
      val raw1 = "10x + 2x - (3x + 6)/3"
      val raw2 = "18*(2x+2)-5"
      val raw3 = "((9x + 81)/3 + 27)/3  - 2x"
      val raw4 = "18x + (12x + 10)*(2x+4)/2 - 5x"
      val raw5 = "(2x+5) * (x*(9x + 81)/3 + 27)/(1+1+1)  - 2x"
      val raw6 = "(2x+5) * (x*(9x^3 + 81)/3 + 27)/(1+1+1)  - 2x  "
      
      def norm(s: String) = s.replaceAll("\\s", "")
      
      it("10x") {
        R.runLen(monom)(raw1) shouldBe
          Right((NP(10, 1), 3))
      }
      it("18") {
        R.runLen(monom)(raw2) shouldBe
          Right((NP(18, 0), 2))
      }
      it("18:2") {
        val r = R.runLen(build)(raw2)
        pprint.log(r)
      }
      it("raw3") {
        val r = R.runLen(build)(norm(raw3))
        pprint.log(r)
      }
      
    }
  }
  
  describe("reproduce the problem: firstNonmatchingIndex") {
    val ab: Parser[(String, String)] = "abc" ** "def"
    it("worked#1") {
      R.runLen("abc")("abc") shouldBe 
        Right("abc", 3)
    }
    it("worked#2") {
      R.runLen(ab)("abcdef") shouldBe 
        Right((("abc", "def"), 6))
    }
    it("fixed#1") {
      R.runLen(ab)("abc") shouldBe
        Left(ParseError(List((Location("abc", 3), "'def'"))))
    }
    it("fixed#2") {
      R.runLen(ab)("abcde") shouldBe
        Left(ParseError(List((Location("abcde", 5), "'def'"))))
    }
  }
  
  /**
    * https://en.wikipedia.org/wiki/Shunting-yard_algorithm 
    */
  describe("recursive calculator done!") {
    import Algebraic.MathOperationNumberParser._

    it("123") {
      Seq(
        "1+2",
        "1-2",
        "1-2*3",
        "(1-2)*3",
        "((1-2)*(3+4))/5-1",
      ).foreach { s => pprint.log(R.run(build)(s)) }
    }
  }
  
  describe("recursive parsers experiments") {
    it("1:") {
      
      sealed trait Phrase
      case class Word(s: String) extends Phrase
      case class Pair(l: Phrase, r: Phrase) extends Phrase

      val prefix = ("hello" | "goodbye") map Word
      val suffix = ("world" | "seattle") map Word
      val ws = whitespace
      val lp = char('(')
      val rp = char(')')
      
      def parened: Parser[Phrase] = surround(lp, rp)(parser)
      def parser: Parser[Phrase] = ((parened | prefix) ** ws ** (parened | suffix)) map { case ((l, _), r) =>
        Pair(l, r)
      }
      
      val raw = "(hello world) ((goodbye seattle) world)"
      val r = R.run(parser)(raw)
      pprint.log(r)
      
    }
  }
}