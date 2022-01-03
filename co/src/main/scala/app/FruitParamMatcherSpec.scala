package app

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class FruitParamMatcherSpec extends AnyFunSpec with Matchers {

  describe("fruit param matcher") {
    def mkParam(value: String) = Map("f" -> Seq(value))

    it("apple") {
      FruitParamMatcher.unapply(mkParam("Apple")) shouldBe Some(Apple)
    }
    it("plum") {
      FruitParamMatcher.unapply(mkParam("Plum")) shouldBe Some(Plum)
    }
    it("banana") {
      FruitParamMatcher.unapply(mkParam("Banana")) shouldBe None
    }
    it("whatever") {
      FruitParamMatcher.unapply(mkParam("whatever")) shouldBe None
    }
  }

}
