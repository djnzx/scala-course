package mockitox

import org.mockito.IdiomaticMockito
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LearningMockitoSpy extends AnyFunSuite with Matchers with IdiomaticMockito {

  class Calculator {
    def add0(a: Int, b: Int): Int = ???
    def add(a: Int, b: Int): Int = add0(a, b)
  }

  val realCalc = new Calculator

  test("partial mock with Java-ish API") {
    val spyCalc = spy(realCalc)
    org.mockito.Mockito.doReturn(113).when(spyCalc).add0(1, 2)

    val outcome = spyCalc.add(1, 2)

    outcome shouldBe 113
    spyCalc.add0(1, 2) wasCalled once
  }

  test("partial mock with Scala API") {
    val spyCalc = spy(realCalc)
    // but it's not asy to bring it into the scope by mixing traits
    org.mockito.MockitoSugar.doReturn(113).when(spyCalc).add0(1, 2)

    val result = spyCalc.add(1, 2)

    result shouldBe 113
    spyCalc.add0(1, 2) wasCalled once
  }
}
