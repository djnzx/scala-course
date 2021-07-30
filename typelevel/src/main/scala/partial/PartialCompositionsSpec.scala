package partial

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PartialCompositionsSpec extends AnyFunSpec with Matchers {

  /**
    * basic tests for our
    * business the logic implementation
    */
  describe("basic business logic test") {
    import PartialCompositions.{businessLogic => myCode}

    it("success cases") {
      myCode(1) shouldEqual "one"
      myCode(10) shouldEqual "ten"
    }

    it("exception can be thrown") {
      an[IllegalArgumentException] should be thrownBy myCode(2)
      (the [IllegalArgumentException] thrownBy myCode(2)).getMessage should include ("TWO")
    }

    it("can be partial (cover not all the cases)") {
      an [MatchError] should be thrownBy myCode(111)
      (the [MatchError] thrownBy myCode(111)).getMessage should equal ("111 (of class java.lang.Integer)")
    }

  }

  /**
    * basic tests for validation composition
    * rhe results mostly are the same.
    * except 10 which is filtered out because of validation
    * that means that function becomes partial after validation
    */
  describe("1-st composition: business logic + validation") {
    import PartialCompositions.{businessLogicValidated => myCode}

    it("success cases") {
      myCode(1) shouldEqual "one"
    }

    it("exception can be thrown") {
      an[IllegalArgumentException] should be thrownBy myCode(2)
      (the [IllegalArgumentException] thrownBy myCode(2)).getMessage should include ("TWO")
    }

    it("can be partial (cover not all the cases, because of calidated)") {
      an [MatchError] should be thrownBy myCode(10)
      (the [MatchError] thrownBy myCode(10)).getMessage should equal ("10 (of class java.lang.Integer)")
    }

    it("can be partial (cover not all the cases)") {
      an [MatchError] should be thrownBy myCode(111)
      (the [MatchError] thrownBy myCode(111)).getMessage should equal ("111 (of class java.lang.Integer)")
    }

  }

  /**
    * catching exceptions.
    * success cases are wrapped into Right
    * failures are wrapped into Left
    * partials still produces MatchError
    */
  describe("2-nd composition: try catch added") {
    import PartialCompositions.{businessLogicValidatedWithExceptionsCaught => myCode}

    it("success cases") {
      myCode(1) shouldEqual Right("one")
    }

    it("fail cases") {
      myCode(2).isLeft shouldBe true
      myCode(2).swap.getOrElse(???).asInstanceOf[IllegalArgumentException].getMessage shouldEqual "TWO"
    }

    it("it's still partial (cover not all the cases)") {
      val code = () => myCode(3)

      an [MatchError] should be thrownBy code()
      (the [MatchError] thrownBy code()).getMessage should equal ("3 (of class java.lang.Integer)")

      an [MatchError] should be thrownBy myCode(111)
      (the [MatchError] thrownBy myCode(111)).getMessage should equal ("111 (of class java.lang.Integer)")
    }

  }

  describe("3-rd composition: handling matching errors (uncovered cases) at the very end") {
    import PartialCompositions.{reallySafeComposition => myCode}

    it("success cases") {
      myCode(1) shouldEqual Right("one")
    }

    it("fail cases") {
      myCode(2).isLeft shouldBe true
      myCode(2).swap.getOrElse(???).asInstanceOf[IllegalArgumentException].getMessage shouldEqual "TWO"
    }

    it("it's still partial (cover not all the cases)") {
      myCode(3) shouldEqual Right("covered!")
      myCode(111) shouldEqual Right("covered!")
    }

  }
}
